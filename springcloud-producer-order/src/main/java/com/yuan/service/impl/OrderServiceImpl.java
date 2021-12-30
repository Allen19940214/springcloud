package com.yuan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.dao.OrderDao;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }
    @Override
    public Order findById(String id) {
        return orderDao.findById(id);
    }

    @Override
    public int addOrder(Order order) throws JsonProcessingException {
        int i = orderDao.addOrder(order);
        if(i>0){
            CorrelationData correlationData = new CorrelationData(order.getOrderId());
            rabbitTemplate.convertAndSend("ttlDirectExchange","ttlsms",objectMapper.writeValueAsString(order),correlationData);
            return i;
        }
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
        return orderDao.addOrderToBackup(order);
    }

    @Override
    public int updateByIdBackup(Order order) {
        return orderDao.updateByIdBackup(order);
    }
}
