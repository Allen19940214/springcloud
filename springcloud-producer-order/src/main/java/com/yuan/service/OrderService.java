package com.yuan.service;

import com.yuan.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(String id);
    int addOrder(Order order);
    int updateById(Order order);
    int deleteById(String id);
}
