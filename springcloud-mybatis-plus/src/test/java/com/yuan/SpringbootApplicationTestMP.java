package com.yuan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuan.dao.UserMapper;
import com.yuan.pojo.User;
import com.yuan.service.UserMapperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootApplicationTestMP {
    @Autowired
    private UserMapperService userMapperService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserMapper userMapper;
    @Test
    public void test1(){
        User byId = userMapperService.getById(104);
        byId.setUid(104);
        byId.setAge(29);
        userMapperService.saveOrUpdate(byId);
    }
    @Test
    public void test2(){
        User user = new User();
        user.setUid(null);
        user.setPassword("123456");
        user.setUsername("张三");
        user.setEmail("ilzyy1421@163.com");
        user.setAge(18);
        for (int i = 0; i < 50; i++) {
            userMapperService.save(user);
        }
    }
    @Test
    public void test3(){
        List<User> users = userMapperService.listByIds(Arrays.asList(160, 161, 162));
        users.forEach(System.out::println);
    }
    @Test
    public void test4(){
        Page<User> page = new Page<>(1,5);
        Page<User> page1 = userMapperService.page(page);
    }
    @Test
    public void test5(){
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.like("email","163");
        System.out.println(userMapperService.list(objectQueryWrapper));
    }

}
