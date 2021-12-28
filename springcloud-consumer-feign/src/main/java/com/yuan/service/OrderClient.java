package com.yuan.service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderClient {
    @RabbitListener(queues = "FanoutQueue1")
    public void getOrder(String order){
        System.out.println("消费者接收到订单信息并准备派单:>>"+order);
    }
}
