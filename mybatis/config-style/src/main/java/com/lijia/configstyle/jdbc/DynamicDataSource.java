package com.lijia.configstyle.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author : 李佳
 * @Description : 动态数据源
 * @Date : 2019/3/29 0029
 * @Email : 502766817@qq.com
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public static final String DEFAULT_DS = "dataSourceMaster";

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println("数据源为:"+CONTEXT_HOLDER.get());
        return CONTEXT_HOLDER.get();
    }

    /** 设置数据源名*/
    public static void setDB(String dbType) {
        System.out.println("切换到{"+dbType+"}数据源");
        CONTEXT_HOLDER.set(dbType);
    }

    /** 获取数据源名*/
    public static String getDB() {
        return (CONTEXT_HOLDER.get());
    }

    /** 清除数据源名*/
    public static void clearDB() {
        CONTEXT_HOLDER.remove();
    }

}
