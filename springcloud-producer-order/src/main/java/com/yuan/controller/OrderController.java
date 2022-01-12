package com.yuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Value("${server.port}")
    private String port;
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
    //分页查询全部
    @RequestMapping("/findAll/{pageNum}/{pageSize}")
    public String findAll(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize")Integer pageSize) throws JsonProcessingException {
        List<Order> allOrder = orderService.findAll(pageNum,pageSize);
        return objectMapper.writeValueAsString("服务端口>>:"+port+allOrder);
    }
    @RequestMapping("/findById/{id}")
    public String findById(@PathVariable("id") String id) throws JsonProcessingException {
        Order byId = orderService.findById(id);
        return objectMapper.writeValueAsString(byId+"order服务>>"+port);
    }
    @RequestMapping("/updateById")
    public String updateById(@RequestBody Order order) {
        Integer i = orderService.updateById(order);
        return i.toString();
    }
    @RequestMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") String id) {
        int i = orderService.deleteById(id);
        if(i>0){
            return "删除成功";
        }
        return "删除失败";
    }
    @RequestMapping("/addOrderToBackup")
    public String addOrderToBackup(@RequestBody Order order) {
        Integer i = orderService.addOrderToBackup(order);
        return i.toString();
    }
    @RequestMapping("/updateByIdBackup")
    public String updateByIdBackup(@RequestBody Order order) {
        Integer i = orderService.updateByIdBackup(order);
        return i.toString();
    }
    @RequestMapping("/selectByCondition")//id
    public String selectByCondition(@RequestBody Map map) throws JsonProcessingException {
        if(map.size()>0){
            System.out.println(map.keySet());
        }
        List<Order> orders = orderService.selectByCondition(map);
        String s = objectMapper.writeValueAsString(orders);
        return s;
    }
}
