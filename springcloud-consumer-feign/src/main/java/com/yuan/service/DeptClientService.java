package com.yuan.service;

import com.yuan.pojo.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "SPRINGCLOUD-PRODUCER-DEPT",fallbackFactory = DeptClientServiceFallback.class)
public interface DeptClientService {
    @GetMapping("/findAll")
     String findAllDept();
    @GetMapping("/findById/{id}")
     String findById(@PathVariable("id") Integer id);
    @PostMapping("/addDept")
     String addDept(@RequestBody Dept dept);
    @PostMapping("/updateById")
     String updateById(@RequestBody Dept dept);
    @GetMapping("/deleteById/{id}")
     String deleteById(@PathVariable("id") Integer id);
}
