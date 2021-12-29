package com.yuan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.dao.OrderDao;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import com.yuan.utils.UUIDUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
    @Autowired
    OrderDao orderDao;
    @Test
    public void testFanoutExchange() throws JsonProcessingException {
        //模拟用户下单
        Order order = new Order(UUIDUtil.getUUID(), 2, 1, 10.0, 1);
        orderService.addOrder(order);
            //下单成功投递消息
        rabbitTemplate.convertAndSend("orderFanoutExchange","",objectMapper.writeValueAsString(order));

    }

    @Test
    public void testDirectExchange() throws JsonProcessingException {
        //rabbitmq路由模式
        //模拟用户下单 成功后给短信 微信 邮件队列投送消息
        //下单成功投递消息
        rabbitTemplate.convertAndSend("orderDirectExchange","sms","下单成功了");
        rabbitTemplate.convertAndSend("orderDirectExchange","wechat","下单成功了");
    }

    @Test
    public void testDirectExchangeTTL() throws JsonProcessingException {
        //rabbitmq路由模式+延时队列
        //模拟用户下单 成功后给短信 微信 邮件队列投送消息
        //下单成功投递消息
        rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms","下单成功了");
    }
    @Test
    public void testDirectExchangeTTL1() throws JsonProcessingException {
        //rabbitmq路由模式+延时消息（对某条消息设置过期时间，如果大余队列整体过期时间，会以队列的过期时间执行,比队列小则以小的为准）
        //模拟用户下单 成功后给测试队列投送消息
        //下单成功投递消息
        MessagePostProcessor messagePostProcessor=new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("5000");
                return message;
            }
        };
        rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms","下单成功了",messagePostProcessor);
    }
    @Test
    public void testBackupTable() throws JsonProcessingException {
        //测试本地冗余表 插入and更新
        /*Order order = new Order(UUIDUtil.getUUID(), 2, 1, 10.0, 1);
        orderDao.addOrderToBackup(order);*/
        Order order = new Order("cacd2607ec5", 2, 1, 10.0, 0);
        orderDao.updateByIdBackup(order);
    }

    @Test
    public void testSendCallback() throws JsonProcessingException {
        Order order = new Order(UUIDUtil.getUUID(), 2, 1, 10.0, 1);
        orderService.addOrder(order);
    }
}
