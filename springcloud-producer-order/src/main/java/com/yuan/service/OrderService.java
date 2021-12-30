package com.yuan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuan.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(String id);
    int addOrder(Order order) throws JsonProcessingException;
    int updateById(Order order);
    int deleteById(String id);

    int addOrderToBackup(Order order);
    int updateByIdBackup(Order order);
}
