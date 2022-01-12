package com.yuan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuan.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> findAll(Integer pageNum,Integer pageSize);
    Order findById(String id);
    int addOrder(Order order) throws JsonProcessingException;
    int updateById(Order order);
    int deleteById(String id);

    int addOrderToBackup(Order order);
    int updateByIdBackup(Order order);
    List<Order> selectByCondition(Map map);
}
