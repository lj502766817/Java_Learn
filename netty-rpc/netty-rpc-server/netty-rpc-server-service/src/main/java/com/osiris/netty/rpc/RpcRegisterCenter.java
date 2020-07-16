package com.osiris.netty.rpc;

import com.osiris.netty.rpc.dto.RpcRequest;
import com.osiris.netty.rpc.registry.RegistryCenter;
import com.osiris.netty.rpc.registry.RegistryCenterWithZk;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijia
 */
public class RpcRegisterCenter implements ApplicationContextAware, InitializingBean {

    private Integer port;

    private Map<String, Object> registerMap = new ConcurrentHashMap<>(16);

    private RegistryCenter registryCenter = new RegistryCenterWithZk();

    public RpcRegisterCenter(Integer port) {
        this.port = port;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("netty开始侦听端口:"+port);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        //自定义协议解码器
                        // 入参有 5 个， 分别解释如下
                        //maxFrameLength： 框架的最大长度。 如果帧的长度大于此值， 则将抛出 TooLongFrameException。
                        //lengthFieldOffset： 长度字段的偏移量： 即对应的长度字段在整个消息数据中得位置
                        //lengthFieldLength： 长度字段的长度。 如： 长度字段是 int 型表示， 那么这个值就是 4（long 型就是 8）
                        //lengthAdjustment： 要添加到长度字段值的补偿值
                        //initialBytesToStrip： 从解码帧中去除的第一个字节数
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        //自定义协议编码器
                        pipeline.addLast(new LengthFieldPrepender(4));
                        //对象参数类型编码器
                        pipeline.addLast("encoder", new ObjectEncoder());
                        //对象参数类型解码器
                        pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.cacheDisabled(null)));
                        pipeline.addLast(new RegistryHandler(registerMap));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture channelFuture = b.bind(port).sync();
        channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcRegister.class);
        String serviceAdress = getAdresss();
        for (Object bean : beans.values()) {
            String serviceName = bean.getClass().getInterfaces()[0].getName();
            registerMap.put(serviceName, bean);
            registryCenter.registService(serviceName,serviceAdress+":"+port);
        }

    }

    /**
     * 获得本机ip
     */
    private String getAdresss() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class RegistryHandler extends ChannelInboundHandlerAdapter {

        private Map<String, Object> registerMap;

        public RegistryHandler(Map<String, Object> registerMap) {
            this.registerMap = registerMap;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            RpcRequest request = (RpcRequest) msg;
            Object result = dohandleMsg(request, registerMap);
            if (result!=null){
                ctx.write(result);
            }
            ctx.flush();
            ctx.close();
        }

        private Object dohandleMsg(RpcRequest request, Map<String, Object> registerMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Object result = null;
            Object instanse = registerMap.get(request.getClassName());
            if (instanse != null) {
                Method method = instanse.getClass().getMethod(request.getMethodName(), request.getParams());
                result = method.invoke(instanse, request.getValue());
            }
            return result;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
