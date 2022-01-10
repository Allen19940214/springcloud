package com.yuan.service.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling//开启定时任务
@Slf4j
public class TimingTaskService implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{
    @Autowired
    private OrderService orderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @PostConstruct
    public void init(){
        //将自定义接口实现类注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
    //对本地消息冗余表中发送失败的消息 进行重新发送
    @Scheduled(cron = "0/59 * * * * ? ")
    public void sendFailOrder() throws JsonProcessingException {
        log.info("定时任务：发送冗余表中投递失败的消息（状态为0的）");
        Map map = new HashMap<>();
        map.put("mqStatus", 0);
        //获得冗余订单集合 并重新投递
        List<Order> orders = orderService.selectByCondition(map);
        if (orders.size() == 0) {
            log.info("暂无需要重新投递的消息");
        }else {
            for (Order order : orders) {
                CorrelationData correlationData = new CorrelationData();
                String s = objectMapper.writeValueAsString(order);
                byte[] bytesOrder = s.getBytes();
                correlationData.setReturnedMessage(new Message(bytesOrder));
                log.info("查询到失败消息为{}：", order);
                rabbitTemplate.convertAndSend("ttlDirectExchange", "ttlsms", objectMapper.writeValueAsString(order), correlationData);
            }
        }
        //遗留问题：消费者多个地方使用ack如何解决，比如在这个定时任务中
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            try {
                //获取订单
                log.error("订单定时任务发送失败:{},失败原因为:{}", ack, cause);
                byte[] body = correlationData.getReturnedMessage().getBody();
                String sOrder = new String(body);
                Order order = objectMapper.readValue(sOrder, new TypeReference<Order>() {
                });
                log.info("发送的消息体为：{}", order);
                log.info("通知人工干预！！");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("消息发送失败，并且人干预业务异常");
            }
        }
        //成功，则需将消息添加到本地消息冗余表或者更新状态
        if (ack) {
            try {
                //获取订单
                log.info("订单发送成功:{},成功cause为:{}", ack, cause);
                byte[] body = correlationData.getReturnedMessage().getBody();
                String sOrder = new String(body);
                Order order = objectMapper.readValue(sOrder, new TypeReference<Order>(){});
                log.info("发送的消息体为：{}", order);
                order.setMqStatus(1);
                orderService.updateByIdBackup(order);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("定时任务消息发送成功，但是修改冗余消息是出现异常");
            }
        }
    }
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        //走到这里说明交换机路由到队列时肯定失败 可选择做一些操作 对退回的消息做标记
        log.error("定时任务消息{}，被交换机{}退回，退回原因为：{}，路由key：{}",
                new String(message.getBody()),exchange,replyText,routingKey);
    }
}
