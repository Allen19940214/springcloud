package com.yuan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuan.dao.OrderMapper;
import com.yuan.pojo.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootApplicationOrderSeataTest {
    @Autowired
    OrderMapper orderMapper;
    @Test
    public void test1(){
        orderMapper.selectList(null).toString();
    }
    @Test
    public void test2(){
        QueryWrapper<Order> objectQueryWrapper = new QueryWrapper<>();

        orderMapper.selectOne(objectQueryWrapper);
    }
}
