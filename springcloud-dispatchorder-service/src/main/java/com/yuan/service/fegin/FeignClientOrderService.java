package com.yuan.service.fegin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuan.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@FeignClient(value = "springcloud-producer-order",fallbackFactory = OrderServiceFallback.class)
public interface FeignClientOrderService {
    @RequestMapping("/addOrder")
    public String addOrder(@RequestBody Order order);
    @RequestMapping("/findAll")
    public String findAll() throws JsonProcessingException;
    @RequestMapping("/findById/{id}")
    public String findById(@PathVariable("id") String id);
    @RequestMapping("/updateById")
    public String updateById(@RequestBody Order order);
    @RequestMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") String id);
    @RequestMapping("/addOrderToBackup")
    public String addOrderToBackup(@RequestBody Order order);
    @RequestMapping("/updateByIdBackup")
    public String updateByIdBackup(@RequestBody Order order);
    @RequestMapping("/selectByCondition")//id
    public String selectByCondition(@RequestBody Map map);
}
