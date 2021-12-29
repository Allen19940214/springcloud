package com.yuan.service.impl;

import com.yuan.dao.OrderDao;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }
    @Override
    public Order findById(String id) {
        return orderDao.findById(id);
    }

    @Override
    public int addOrder(Order order) {
        int i = orderDao.addOrder(order);
        if(i>0){
            rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms",order);
            //可靠消费 用ack确认消息是否投递成功
            rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    System.out.println("cause:"+cause);
                    String id = correlationData.getId();
                    System.out.println("id"+id);
                    if(!ack){
                        //投递失败执行其他操作
                        System.out.println("消息投递失败");
                        return;
                    }
                    System.out.println(ack);
                    //成功则向本地消息冗余表存放订单并修改状态
                    try {
                    int i1 = orderDao.addOrderToBackup(order);
                    orderDao.updateByIdBackup(new Order(order.getOrderId(),0));
                    if(i1==1){
                        System.out.println("本地消息表修改状态成功 0代表投递成功 1为失败");
                    }
                    } catch (Exception e) {
                        System.out.println("本地消息修改状态失败"+e.getMessage());
                    }
                }
            });
            return i;
            //仅返回下单成功信息
        }
        System.out.println("下单失败");
        return 0;
    }

    @Override
    public int updateById(Order order) {
        return orderDao.updateById(order);
    }

    @Override
    public int deleteById(String id) {
        return orderDao.deleteById(id);
    }

    @Override
    public int addOrderToBackup(Order order) {
        return 0;
    }

    @Override
    public int updateByIdBackup(Order order) {
        return 0;
    }
}
