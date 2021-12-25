package com.yuan.controller;

import com.yuan.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class RibbonTestController {
    @Autowired
    RestTemplate restTemplate;

    //String url="http://localhost:8001";集成Ribbon做负载均衡后url需要改为项目名访问
    private static final String url="http://SPRINGCLOUD-PRODUCER-DEPT";
    @GetMapping("/dept/findAllDept")
    public String findAllDept(){
        String s = restTemplate.getForObject(url + "/findAll" , String.class);
        return s;
    }
    @GetMapping("/dept/findById/{id}")
    public String findById(@PathVariable("id") Integer id){
        String s = restTemplate.getForObject(url + "/findById/" + id, String.class);
        return s;
    }
    @PostMapping("/dept/addDept")
    public String addDept(@RequestBody Dept dept){
        String message = restTemplate.postForObject(url + "/addDept/",dept,String.class);
        return message;
    }
    @PostMapping("/dept/updateById")
    public String updateById(@RequestBody Dept dept){
        String message = restTemplate.postForObject(url + "/updateById/", dept, String.class);
        return message;
    }
    @GetMapping("/dept/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id){
        String message = restTemplate.getForObject(url + "/deleteById/" + id, String.class);
        return message;
    }
}
