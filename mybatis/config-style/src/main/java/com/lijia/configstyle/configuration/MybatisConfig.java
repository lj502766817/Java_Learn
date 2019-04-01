package com.lijia.configstyle.configuration;

import com.lijia.configstyle.plugins.MyPlugin;
import com.lijia.configstyle.typeHandlers.MyTypeHandler;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author : 李佳
 * @Description : Mybatis配置类
 * @Date : 2019/3/27 0027
 * @Email : 502766817@qq.com
 */
@Configuration
@MapperScan("com.lijia.configstyle.mapper")
@EnableTransactionManagement(proxyTargetClass = true /*true 表示使用cglib的代理方式,false表示使用jvm的默认代理方式*/)
public class MybatisConfig {

    @Autowired
    @Qualifier("myDynamicDataSource")
    public DataSource dataSource;

    @Lazy(value = false)
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory localSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeHandlers(new TypeHandler[]{new MyTypeHandler()});
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new MyPlugin()});
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        sqlSessionFactory.getConfiguration().setLogImpl(Slf4jImpl.class);

        return sqlSessionFactory;
    }

    @Bean(name = "txManager")
    public DataSourceTransactionManager dataSourceTransactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

}
