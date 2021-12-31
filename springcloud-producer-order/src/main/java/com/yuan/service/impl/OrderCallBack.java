package com.yuan.service.impl;

import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
//可靠生产实现：交换机确认接口（RabbitTemplate.ConfirmCallback）+消息回退接口(RabbitTemplate.ReturnCallback)
public class OrderCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderService orderService;
    @PostConstruct
    public void init(){
        //将自定义接口实现类注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
    //correlationData为自定义发送的数据，ack是否成功接收，cause失败原因,成功为空（确认交换机是否收到消息）
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(!ack){
            log.error("发送的订单id为：{}，发送失败:{},失败原因为:{}",correlationData.getId(),ack,cause);
            //失败进行其他补偿操作（比如对消息冗余表中未成功发送的消息定时重新发送，MqStatus属性：成功为1，失败则修改状态为0 ）
            try {
                Order order = orderService.findById(correlationData.getId());
                order.setMqStatus(0);
                Map map =new HashMap<>();
                map.put("orderId", correlationData.getId());
                //查看冗余表是否存过数据 有的话直接更新状态，没有则新增
                List<Order> orders = orderService.selectByCondition(map);
                if(orders.size()>0){
                    orderService.updateByIdBackup(order);
                }else {
                    orderService.addOrderToBackup(order);
                }
            } catch (Exception e) {
                log.error("消息发送失败，并且添加冗余消息/更新冗余信息状态是出现异常:"+e.getMessage());
            }
            return;
        }
        //成功，也需将消息添加到本地消息冗余表或者更新状态
        if(ack){
            log.info("发送的订单id为：{}，发送成功:{},失败原因为:{null}",correlationData.getId(),ack,cause);
            try {
                Order order = orderService.findById(correlationData.getId());
                order.setMqStatus(1);
                Map map =new HashMap<>();
                map.put("orderId", correlationData.getId());
                //查看冗余表是否存过数据 有的话直接更新状态
                List<Order> orders = orderService.selectByCondition(map);
                if(orders.size()>0){
                    orderService.updateByIdBackup(order);
                }else {
                    orderService.addOrderToBackup(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("消息发送成功，但是添加冗余消息是出现异常");
            }
        }
    }
    /*
    Mandatory参数
    消息确认机制为交换机的机制，只要交换机收到消息，便视为成功，但是交换机将消息路由给队列时出现问题，消息也将会丢失，
    所以可以使用消息回退，即如果交换机在将消息路由到队列时出现问题，导致队列没有收到消息，交换机需要将消息回退给生产者。
    需配置publisher-returns: true ，并实现回退接口
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        //走到这里说明交换机路由到队列时肯定失败 可选择做一些操作 对退回的消息做标记
        log.error("消息{}，被交换机{}退回，退回原因为：{}，路由key：{}",
                new String(message.getBody()),exchange,replyText,routingKey);
    }
}
