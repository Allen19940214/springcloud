package com.yuan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
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
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    public void testFanoutExchange() throws JsonProcessingException {
        //模拟用户下单
        Order order = new Order(UUIDUtil.getUUID(), 2, 1, 10.0, 1,"方便面");
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
        String s = objectMapper.writeValueAsString("下单成功了");
        rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms",s);
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
        Order order = new Order("cacd2607ec5", 2, 1, 10.0, 0,"方便面");
        orderDao.updateByIdBackup(order);
    }

    @Test
    public void testSendCallback() throws JsonProcessingException {
        for (int i = 0; i <=20; i++) {
            Order order = new Order(UUIDUtil.getUUID(), 2, 1, 10.0, 1,"方便面");
            orderService.addOrder(order);
        }
    }
    //优先级测试
    @Test
    public void testMax() throws JsonProcessingException, InterruptedException {
        MessagePostProcessor messagePostProcessor=new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置优先级为5
                message.getMessageProperties().setPriority(5);
                return message;
            }
        };
        MessagePostProcessor messagePostProcessor1=new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置优先级为9
                message.getMessageProperties().setPriority(9);
                return message;
            }
        };
        rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms","第一条消息优先级为5",messagePostProcessor);
        TimeUnit.SECONDS.sleep(3);
        rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms","第二条消息优先级为9",messagePostProcessor1);

    }
    @Test
    public void testSendOrder() throws JsonProcessingException {
        //给message唯一id解决幂等问题（发送到消费者，可以从在消费者用message取出）或者MessageBuilder
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
                return message;
            }
        };

        CorrelationData correlationData = new CorrelationData();
        //订单的唯一id
        String uuid = UUIDUtil.getUUID();
        Order order = new Order(uuid, 2, 1, 10.0, 1,"方便面");
        //将订单转化为json 再转化为byte[]数组
        String sorder = objectMapper.writeValueAsString(order);
        byte[] bytesOrder  = sorder.getBytes();
        correlationData.setReturnedMessage(new Message(bytesOrder));
        rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms",objectMapper.writeValueAsString(order),messagePostProcessor,correlationData);
    }
}
