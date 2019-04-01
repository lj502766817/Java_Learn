package com.lijia.configstyle.jdbc;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author : 李佳
 * @Description : 数据源切换切面
 * @Date : 2019/3/29 0029
 * @Email : 502766817@qq.com
 */
@Aspect
@Order(-10)
@Component
public class DynamicDataSourceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(TargetDataSource)")
    public void doBefore(JoinPoint joinPoint){
        //获得当前访问的class
        Class<?> className = joinPoint.getTarget().getClass();
        //获得访问的方法名
        String methodName = joinPoint.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)joinPoint.getSignature()).getParameterTypes();

        String dataSourceKey = "dataSourceMaster";

        try {
            //得到访问的对象方法
            Method method = className.getMethod(methodName,argClass);
            // 判断是否存在@TargetDataSource注解
            if (method.isAnnotationPresent(TargetDataSource.class)){
                //拿到注解中的值
                TargetDataSource annotation = method.getAnnotation(TargetDataSource.class);
                dataSourceKey = annotation.dataSouceKey();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            LOGGER.error("数据源切换切面异常,使用默认数据源");
        }
        LOGGER.info("使用数据源:{}",dataSourceKey);
        DynamicDataSource.setDB(dataSourceKey);
    }

    @After("@annotation(TargetDataSource)")
    public void doAfter(){
        LOGGER.info("数据源:{},执行清理方法",DynamicDataSource.getDB());
        DynamicDataSource.clearDB();
    }

}
