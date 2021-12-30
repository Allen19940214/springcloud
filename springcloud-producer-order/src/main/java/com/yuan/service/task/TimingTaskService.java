package com.yuan.service.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling//开启定时任务
@Slf4j
public class TimingTaskService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    //对本地消息冗余表中发送失败的消息 进行重新发送
    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendFailOrder() throws JsonProcessingException {
        log.info("定时任务：发送冗余表中投递失败的消息（状态为0的）");
        Map map =new HashMap<>();
        map.put("mqStatus",0);
        //获得冗余订单集合 并重新投递
        List<Order> orders = orderService.selectByCondition(map);
        if(orders.size()==0){
            log.info("暂无需要重新投递的消息");
            return;
        }
        for (Order order : orders) {
            log.info("查询到失败消息为{}：",order);
            CorrelationData correlationData = new CorrelationData(order.getOrderId());
            rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms",objectMapper.writeValueAsString(order),correlationData);
        }
    }
}
