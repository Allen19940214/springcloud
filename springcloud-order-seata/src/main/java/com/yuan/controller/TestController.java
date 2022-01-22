package com.yuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.dao.OrderMapper;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import com.yuan.api.StockFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    StockFeignClient stockFeignClient;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderService orderService;
    @RequestMapping("/addOrder")
    public String addOrder(@RequestBody Order order) throws JsonProcessingException {
        boolean save = orderService.save(order);
        return objectMapper.writeValueAsString(save);
    }
}
