package com.osiris.netty.rpc;

import com.osiris.netty.rpc.dto.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lijia at 2020-07-09
 * @Description
 * @Email lijia@ule.com
 */
public class RpcClient implements InvocationHandler {

    private String host;

    private Integer port;

    private Class clazz;

    public RpcClient(Class clazz, String host, Integer port) {
        this.host = host;
        this.port = port;
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(clazz.getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setValue(args);
        Class<?>[] params = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            params[i] = args[i].getClass();
        }
        rpcRequest.setParams(params);

        final ClientHandler clientHandler = new ClientHandler();

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //自定义协议解码器
                            /** 入参有 5 个， 分别解释如下
                             maxFrameLength： 框架的最大长度。 如果帧的长度大于此值， 则将抛出 TooLongFrameException。
                             lengthFieldOffset： 长度字段的偏移量： 即对应的长度字段在整个消息数据中得位置
                             lengthFieldLength： 长度字段的长度。 如： 长度字段是 int 型表示， 那么这个值就是 4（long 型就是 8）
                             lengthAdjustment： 要添加到长度字段值的补偿值
                             initialBytesToStrip： 从解码帧中去除的第一个字节数
                             */
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            //自定义协议编码器
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //对象参数类型编码器
                            pipeline.addLast("encoder", new ObjectEncoder());
                            //对象参数类型解码器
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(clientHandler);
                        }
                    });
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().writeAndFlush(rpcRequest).sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
        return clientHandler.getResponse();
    }

    private class ClientHandler extends ChannelInboundHandlerAdapter {

        private Object response;

        public Object getResponse() {
            return response;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            this.response = msg;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }
}
