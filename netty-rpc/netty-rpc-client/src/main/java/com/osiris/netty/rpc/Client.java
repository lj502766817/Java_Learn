package com.osiris.netty.rpc;


import com.osiris.netty.rpc.dto.User;
import com.osiris.netty.rpc.service.HelloService;
import com.osiris.netty.rpc.service.MathService;

/**
 * @author lijia
 */
public class Client {

    public static void main(String[] args) {
        User user = new User();
        user.setAge(18);
        user.setName("lijia");
        HelloService helloService = RpcProxy.getServcie(HelloService.class,"localhost",8080);
        helloService.sayHello(user);
        MathService mathService = RpcProxy.getServcie(MathService.class,"localhost",8080);
        System.out.println(mathService.add(1,3));
        System.out.println(mathService.mult(11d,1.1d));
    }

}
