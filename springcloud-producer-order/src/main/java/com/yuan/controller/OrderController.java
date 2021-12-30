package com.yuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/addOrder")
    public String addOrder(@RequestBody Order order) throws JsonProcessingException {
        int i = orderService.addOrder(order);
        if(i>0){
            return "下单成功";
        }
        return "下单失败";
    }
    @RequestMapping("/findAll")
    public String findAll() throws JsonProcessingException {
        List<Order> allOrder = orderService.findAll();
        return objectMapper.writeValueAsString(allOrder);
    }
    @RequestMapping("/findById")
    public String findById(String id) throws JsonProcessingException {
        Order byId = orderService.findById(id);
        return objectMapper.writeValueAsString(byId);
    }
    @RequestMapping("/updateById")
    public String updateById(Order order) {
        Integer i = orderService.updateById(order);
        return i.toString();
    }
    @RequestMapping("/deleteById")
    public String deleteById(String id) {
        Integer i = orderService.deleteById(id);
        return i.toString();
    }
    @RequestMapping("/addOrderToBackup")
    public String addOrderToBackup(Order order) {
        Integer i = orderService.addOrderToBackup(order);
        return i.toString();
    }
    @RequestMapping("/updateByIdBackup")
    public String updateByIdBackup(Order order) {
        Integer i = orderService.updateByIdBackup(order);
        return i.toString();
    }
}
