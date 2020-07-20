package com.osiris.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;

import java.io.IOException;


/**
 * @author lijia at 2020-07-16
 * @Description zookeeper监听事件
 */
public class WatchDemo {

    public static void main(String[] args) throws Exception {
//        addListenerToNode();
        zkClientWatcher();
        System.in.read();
    }

    /**
     * 客户端注册watcher有3种方式，getData、exists、getChildren
     */
    private static void zkClientWatcher() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(MyConstants.ZOOKEEPER_CONNECTION_STR, 20000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("event.type:"+event.getType());
            }
        });
        zooKeeper.create("/wathcher1","00".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.exists("/wathcher1",true);
        Thread.sleep(1000);
        zooKeeper.setData("/wathcher1","upd".getBytes(),-1);
    }


    /**
     * curator监听某个节点及子节点的cud事件
     */
    private static void addListenerToNode() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                //设置连接地址
                .connectString(MyConstants.ZOOKEEPER_CONNECTION_STR)
                //连接过期时间
                .sessionTimeoutMs(5000)
                //连接重试机制,当前机制是阶次重试,第一次间隔1秒,一共重试2次
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        //启动了,并不一定连接成功
        curatorFramework.start();
        //阻塞直到连接成功,如果不调这个方法,假如网络不行或者其他原因,在没连上的时候去做crud会报错连接不上
        try {
            curatorFramework.blockUntilConnected();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CuratorCache curatorCache = CuratorCache.build(curatorFramework,"/test2");
        CuratorCacheListener curatorCacheListener = (type, oldData, data) -> System.out.println("接收到节点事件:"+type+",子节点旧值:"+oldData+",子节点新值:"+data);
        curatorCache.listenable().addListener(curatorCacheListener);
        curatorCache.start();
        System.out.println("开始监听------");
    }



}
