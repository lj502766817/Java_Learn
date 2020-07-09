package com.osiris.netty.rpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijia at 2020-07-09
 * @Description
 * @Email lijia@ule.com
 */
@Configuration
@ComponentScan(basePackages = "com.osiris.netty.rpc")
public class SpringConfig {

    @Bean(name = "rpcRegisterCenter")
    public RpcRegisterCenter getRpcRegisterCenter(){
        return new RpcRegisterCenter(8080);
    }

}
