package com.yuan.controller;

import com.yuan.pojo.Order;
import com.yuan.service.fegin.FeignClientOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private FeignClientOrderService OrderService;

    @RequestMapping("/addOrderToBackup")
    public String addOrderToBackup(@RequestBody Order order) {
        return OrderService.addOrderToBackup(order);
    }
    @RequestMapping("/updateByIdBackup")
    public String updateByIdBackup(@RequestBody Order order) {
        return OrderService.updateByIdBackup(order);
    }

}
