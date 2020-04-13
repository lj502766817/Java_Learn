package com.lj.demo.websocket.controller;

import com.lj.demo.websocket.service.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : 李佳
 * @Description : demo
 * @Date : 2020/3/31 0031
 * @Email : 502766817@qq.com
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/index")
    public String index(){
        return "/page/webSocket.html";
    }

    @RequestMapping("/pushToClient")
    @ResponseBody
    public String pushToClient(@RequestParam("uid") String uid, @RequestParam("msg") String msg){
        WebSocketServer.sengMsg(uid,msg);
        return "success";
    }

}
