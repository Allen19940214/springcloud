package com.yuan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDispatchService {
    @RabbitListener(queues = "deadSmsQueue")
    public void getOrder(String order){
        log.info("监听到死信队列deadSmsQueue，消息为{}",order);
    }
    @RabbitListener(queues = "ttlSmsQueue")
    public void getOrder1(String order){
        log.info("监听到正常队列ttlSmsQueue，消息为{}",order);
    }
}
