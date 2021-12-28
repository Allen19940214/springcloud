package com.yuan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import com.yuan.utils.UUIDUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootOrderTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void test1() throws JsonProcessingException {
        //模拟用户下单
        Order order = new Order(UUIDUtil.getUUID(), 2, 1, 10.0, 1);
        int i = orderService.addOrder(order);
        if(i>0){
            //下单成功投递消息
            rabbitTemplate.convertAndSend("orderFanoutExchange","",objectMapper.writeValueAsString(order));
        }
    }
}
