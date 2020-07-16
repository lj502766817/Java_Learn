package com.osiris.netty.rpc.discover;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lijia at 2020-07-16
 * @Description zookeeper服务发现
 */
public class DiscoverServiceWithZkImpl implements DiscoverService {

    private List<String> serviceHostPorts = new ArrayList<>();

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
    public String discoverHostPort(String serviceName) throws Exception {
         if (serviceHostPorts==null||serviceHostPorts.isEmpty()){
             serviceHostPorts = curatorFramework.getChildren().forPath("/"+serviceName);
             addWatch(serviceName);
         }
        return loadBalanceByRandom(serviceHostPorts);
    }

    private void addWatch(String serviceName) {
        CuratorCache curatorCache = CuratorCache.build(curatorFramework,"/"+serviceName);
        CuratorCacheListener curatorCacheListener = (type, oldData, data) -> {
            try {
                serviceHostPorts = curatorFramework.getChildren().forPath("/"+serviceName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        curatorCache.listenable().addListener(curatorCacheListener);
        curatorCache.start();
    }

    private String loadBalanceByRandom(List<String> serviceHostPorts) {
        if (serviceHostPorts==null||serviceHostPorts.isEmpty()){
            throw new NullPointerException("服务未注册");
        }
        if (serviceHostPorts.size()==1){
            return serviceHostPorts.get(0);
        }
        int a = RandomUtils.nextInt(serviceHostPorts.size()-1);
        return serviceHostPorts.get(a);
    }


}
