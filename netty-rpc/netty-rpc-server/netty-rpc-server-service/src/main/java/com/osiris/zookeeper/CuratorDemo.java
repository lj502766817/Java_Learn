package com.osiris.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Date;

/**
 * @author lijia at 2020-07-15
 * @Description zookeeper基本api
 */
public class CuratorDemo {

    public static void main(String[] args) throws Exception {
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
        System.out.println("连接启动"+new Date());
        curatorFramework.blockUntilConnected();
        System.out.println("成功连到zookeeper"+new Date());
//        createData(curatorFramework);
        updateData(curatorFramework);
//        deleteData(curatorFramework);
    }

    private static void createData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/test1/test1_2","123".getBytes());
    }

    private static void updateData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/test2/test2_1","update112".getBytes());
    }

    private static void deleteData(CuratorFramework curatorFramework) throws Exception {
        Stat stat=new Stat();
        String value=new String(curatorFramework.getData().storingStatIn(stat).forPath("/test1/test1_2"));
        //基于乐观锁删除
        curatorFramework.delete().withVersion(stat.getVersion()).forPath("/test1/test1_2");
    }

}
