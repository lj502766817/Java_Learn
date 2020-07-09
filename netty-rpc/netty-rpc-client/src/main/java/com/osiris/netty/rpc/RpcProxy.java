package com.osiris.netty.rpc;

import java.lang.reflect.Proxy;

/**
 * @author lijia at 2020-07-09
 * @Description
 * @Email lijia@ule.com
 */
public class RpcProxy {

    public static <T> T getServcie(final Class<T> clazz, final String host, final Integer port){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},new RpcClient(clazz,host,port));
    }

}
