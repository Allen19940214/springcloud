package com.yuan.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderClient {
    @RabbitListener(queues = "FanoutQueue1")
    public void getOrder1(String order){
        System.out.println("消费者接收到订单信息并准备派单:>>"+order);
    }

    @RabbitListener(queues = "FanoutQueue2")
    public void getOrder2(String order){
        System.out.println("消费者接收到订单信息并准备派单:>>"+order);
    }

    @RabbitListener(queues = "smsQueue")
    public void dispatch1(String order){
        log.info("消费者接收到订单信息并准备派单:>>{}",order);
    }
    @RabbitListener(queues = "weChatQueue")
    public void dispatch2(String order){
        log.info("消费者接收到订单信息并准备派单:>>{}",order);
    }
}
