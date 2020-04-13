package com.lj.demo.websocket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : 李佳
 * @Description : websocket服务
 * @Date : 2020/3/31 0031
 * @Email : 502766817@qq.com
 */
@ServerEndpoint("/websocket/{uid}")
@Component
public class WebSocketServer {

    private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 线程安全的用户群
     *
     */
    private static Map<String,WebSocketServer> users = new ConcurrentHashMap<>();

    private Session session;

    private String uid = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid){
        this.session = session;
        users.put(uid,this);
        logger.info("{}加入用户群,当前用户人数为:{}",uid,users.size());
        this.uid = uid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.info("向{}发送消息 IO异常:{}",uid,e);
        }
    }

    @OnClose
    public void onClose(){
        users.remove(this.getUid());
        logger.info("{}退出用户群,当前用户群数量为:{}",this.getUid(),users.size());
    }

    @OnMessage
    public void onMessage(String message){
        logger.info("收到{}发送的消息{}",uid,message);
    }

    @OnError
    public void onError(Throwable error){
        logger.info("{}发生错误",uid);
        error.printStackTrace();
    }

    public static void sengMsg(String uid ,String msg){
        WebSocketServer user = users.get(uid);
        if (user==null){
            logger.info("{}对应的用户不存在",uid);
            return;
        }
        try {
            user.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            logger.info("向{}发送消息:{}. IO异常:{}",uid,msg,e);
        }
    }

    private void sendMessage(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    public String getUid() {
        return uid;
    }

}
