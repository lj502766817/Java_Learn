package com.osiris.netty.rpc.registry;

import com.osiris.zookeeper.MyConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * @author lijia at 2020-07-16
 * @Description zookeeper注册中心
 */
public class RegistryCenterWithZk implements RegistryCenter {

    private CuratorFramework curatorFramework;
    {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(MyConstants.ZOOKEEPER_CONNECTION_STR)
                .namespace("RpcRegisty")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
        try {
            curatorFramework.blockUntilConnected(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registService(String serviceName, String hostPort) {
        if (StringUtils.isNotBlank(serviceName) && StringUtils.isNotBlank(hostPort)) {
            String servicePath = "/" + serviceName;
            try {
                if (curatorFramework.checkExists().forPath(servicePath) == null) {
                    curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.CONTAINER).forPath(servicePath);
                }
                String adressPath = "/"+hostPort;
                curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(servicePath+adressPath);
                System.out.println(adressPath+"注册成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
