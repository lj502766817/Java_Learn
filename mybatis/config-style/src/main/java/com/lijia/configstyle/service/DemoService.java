package com.lijia.configstyle.service;

import com.lijia.configstyle.jdbc.TargetDataSource;
import com.lijia.configstyle.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 李佳
 * @Description : 多数据源验证
 * @Date : 2019/3/29 0029
 * @Email : 502766817@qq.com
 */
@Service
public class DemoService {

    @Autowired
    private StudentMapper studentMapper;

    @TargetDataSource(dataSouceKey = "dataSourceDev")
    public void test(){
        System.out.println(studentMapper.selectAll());
    }
}
