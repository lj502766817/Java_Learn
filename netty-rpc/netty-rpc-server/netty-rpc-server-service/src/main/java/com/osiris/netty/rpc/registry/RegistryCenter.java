package com.osiris.netty.rpc.registry;

/**
 * @author lijia at 2020-07-16
 * @Description 外部注册中心
 * @Email lijia@ule.com
 */
public interface RegistryCenter {

    public void registService(String serviceName, String hostPort);

}
