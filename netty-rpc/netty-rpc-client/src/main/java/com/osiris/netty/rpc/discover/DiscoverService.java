package com.osiris.netty.rpc.discover;

/**
 * @author lijia at 2020-07-16
 * @Description 服务发现
 * @Email lijia@ule.com
 */
public interface DiscoverService {

    String discoverHostPort(String serviceName) throws Exception;

}
