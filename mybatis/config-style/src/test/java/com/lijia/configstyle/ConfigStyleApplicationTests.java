package com.lijia.configstyle;

import com.lijia.configstyle.jdbc.TargetDataSource;
import com.lijia.configstyle.mapper.StudentMapper;
import com.lijia.configstyle.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigStyleApplication.class)
public class ConfigStyleApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void test1(){
        System.out.println(studentMapper.selectAll());
    }

    @Test
    public void test2(){
        Student student = new Student();
        student.setName("haha");
        student.setAge(21);
        studentMapper.insertSelective(student);

    }
}
