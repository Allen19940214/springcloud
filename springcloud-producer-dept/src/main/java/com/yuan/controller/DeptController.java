package com.yuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yuan.pojo.Dept;
import com.yuan.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
public class DeptController {
    @Autowired
    DeptService deptService;
    @Autowired
    private DiscoveryClient discoveryClient;
    @GetMapping("/getClientInfo")
    public String getClientInfo() {
        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-producer-dept");
        Iterator<ServiceInstance> iterator = instances.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        return instances.toString();
    }
    @GetMapping("/findAll")
    @HystrixCommand(fallbackMethod = "findAllHystrix")//局部熔断 处理异常+超时 熔断方法和原方法参数必须一致
    public String findAllDept() throws JsonProcessingException, InterruptedException {
        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-producer-dept");
        Integer port=null;
        for (ServiceInstance instance : instances) {
            port=instance.getPort();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String dept = objectMapper.writeValueAsString(deptService.findAll()+">>>>>>"+port.toString());
        return dept;
    }
    //局部熔断方法
    public String findAllHystrix() {
        return "访问超时，网络异常";
    }
    @GetMapping("/findById/{id}")
    @HystrixCommand(fallbackMethod = "findByIdHystrix")//局部熔断 处理异常+超时
    public String findById(@PathVariable("id") Integer id) throws JsonProcessingException, InterruptedException {
        //TimeUnit.MILLISECONDS.sleep(5000);
        ObjectMapper objectMapper = new ObjectMapper();
        String dept = objectMapper.writeValueAsString(deptService.findById(id));
        return dept;
    }
    //局部熔断方法
    public String findByIdHystrix(Integer id) {
        return "访问超时，网络异常";
    }
    @PostMapping("/addDept")
    public String addDept(@RequestBody Dept dept) {
        int i = deptService.addDept(dept);
        if(i>0){
            return "添加成功";
        }
        return "添加失败";
    }
    @PostMapping("/updateById")
    public String updateById(@RequestBody Dept dept) {
        int i = deptService.updateById(dept);
        if(i>0){
            return "更新成功";
        }
        return "更新失败";
    }
    @GetMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        int i = deptService.deleteById(id);
        if(i>0){
            return "删除成功";
        }
        return "删除失败";
    }

    public String deptFallbackMethod() {
        return "访问超时:熔断";
    }
}
