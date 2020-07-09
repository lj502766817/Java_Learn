package com.osiris.netty.rpc;

import com.osiris.netty.rpc.dto.User;
import com.osiris.netty.rpc.service.HelloService;

/**
 * @author lijia
 */
@RpcRegister
public class HelloServiceImpl implements HelloService {

    @Override
    public void sayHello(User user) {
        System.out.println("hello :"+ user);
    }
}
