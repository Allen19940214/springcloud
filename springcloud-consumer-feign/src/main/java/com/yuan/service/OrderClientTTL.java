package com.yuan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderClientTTL {
    //监听延时队列
    /*@RabbitListener(queues = "ttlSmsQueue")
    public void dispatch2(String order){
        log.info("消费者接收到订单信息并准备派单:>>{}",order);
    }*/
}
