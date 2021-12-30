package com.yuan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDispatchService {
    @RabbitListener(queues = "deadSmsQueue")
    public void getOrder(String order){
        System.out.println(order);
    }
}
