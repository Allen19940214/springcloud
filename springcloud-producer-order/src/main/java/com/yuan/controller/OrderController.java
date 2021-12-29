package com.yuan.controller;

import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @RequestMapping("/addOrder")
    public String addOrder(@RequestBody Order order){
        int i = orderService.addOrder(order);
        if(i>0){
            return "下单成功";
        }
        return "下单失败";
    }
}
