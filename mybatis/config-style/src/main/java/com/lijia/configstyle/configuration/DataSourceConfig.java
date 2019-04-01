package com.lijia.configstyle.configuration;


import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.lijia.configstyle.jdbc.DynamicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 李佳
 * @Description : Druid数据库源配置
 * @Date : 2019/3/26
 * @Email : 502766817@qq.com
 */
@Configuration
@ComponentScan(basePackages = "com.lijia.configstyle")
@PropertySource("classpath:application.properties")
public class DataSourceConfig {

    @Value("${jdbc.driverClass}")
    private String driverClassName;
    @Value("${jdbc.connectionURL}")
    private String url;
    @Value("${jdbc.userId}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.publicKey}")
    private String publicKey;
    @Value("${jdbc.dev.connectionURL}")
    private String urlDev;

    @Bean
    public Filter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(2000);
        statFilter.setLogSlowSql(true);
        return statFilter;
    }

    /**
     * 详细配置参数说明参考:https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
     * @throws SQLException
     */
    @Bean(name = "dataSourceMaster",initMethod = "init", destroyMethod = "close")//这里的init和close方法都是在druid源码里面的方法
    public DataSource dataSourceMaster() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        //这个设置的作用是当数据库密码加密后,可以通过这个配置对数据库密码进行解密(默认是druid的加密方式,也可以自己手写加密解密方式)
        // (加密命令(cmd):>java -cp D:\java_jar\jar\com\alibaba\druid\1.1.10\druid-1.1.10.jar com.alibaba.druid.filter.config.ConfigTools password)
        dataSource.setConnectionProperties("config.decrypt=true;config.decrypt.key="+publicKey);
        //config表示使用druid默认的解密设置
        dataSource.setFilters("config,stat");
        dataSource.setMaxActive(20);
        dataSource.setInitialSize(20);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(1);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setMaxOpenPreparedStatements(20);
        dataSource.setProxyFilters(Arrays.asList(statFilter()));
        dataSource.setConnectionErrorRetryAttempts(5);
        return dataSource;
    }

    @Bean(name = "dataSourceDev",initMethod = "init", destroyMethod = "close")
    public DataSource dataSourceDev() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(urlDev);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setConnectionProperties("config.decrypt=true;config.decrypt.key="+publicKey);
        dataSource.setFilters("config,stat");
        dataSource.setMaxActive(20);
        dataSource.setInitialSize(20);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(1);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setMaxOpenPreparedStatements(20);
        dataSource.setProxyFilters(Arrays.asList(statFilter()));
        dataSource.setConnectionErrorRetryAttempts(5);
        return dataSource;
    }

    @Bean(name = "myDynamicDataSource")
    public DataSource myDynamicDataSource() throws SQLException {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(dataSourceMaster());
        Map<Object,Object> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("dataSourceMaster",dataSourceMaster());
        dataSourceMap.put("dataSourceDev",dataSourceDev());
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    /**
     * 必须加上static
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer loadProperties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
