package com.yuan.service.fegin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("springcloud-producer-order")
public interface FeignClientOrderService {

    @RequestMapping("/addOrder")
    public String addOrder(@RequestBody Order order);
    @RequestMapping("/findAll")
    public String findAll();
    @RequestMapping("/findById")
    public String findById(String id);
    @RequestMapping("/updateById")
    public String updateById(Order order);
    @RequestMapping("/deleteById")
    public String deleteById(String id);
    @RequestMapping("/addOrderToBackup")
    public String addOrderToBackup(Order order);
    @RequestMapping("/updateByIdBackup")
    public String updateByIdBackup(Order order);
}
