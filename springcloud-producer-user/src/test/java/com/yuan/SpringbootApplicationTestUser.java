package com.yuan;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuan.dao.UserDao;
import com.yuan.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootApplicationTestUser {
    @Autowired
    UserDao userDao;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    DataSourceProperties dataSourceProperties;
    @Test
    public void test1(){
        //获取容器
        //ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
        //创建容器
        //AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        DruidDataSource dataSource = (DruidDataSource)applicationContext.getBean("dataSource");
        System.out.println(dataSource.getInitialSize());
    }

    @Test
    public void testLikeLastName(){
        List list =new ArrayList<>();
        list.add(4);
        list.add(5);
        list.add(6);
        System.out.println(userDao.deleteByList(list));
    }
}