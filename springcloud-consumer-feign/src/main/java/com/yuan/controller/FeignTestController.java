package com.yuan.controller;

import com.yuan.pojo.Dept;
import com.yuan.service.DeptClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeignTestController {
    @Autowired
    DeptClientService deptClientService;

    @GetMapping("/dept/findAllDept")
    public String findAllDept(){
        return deptClientService.findAllDept();
    }
    @GetMapping("/dept/findById/{id}")
    public String findById(@PathVariable("id") Integer id){
        return deptClientService.findById(id);
    }
    @PostMapping("/dept/addDept")
    public String addDept(@RequestBody Dept dept){
        return deptClientService.addDept(dept);
    }
    @PostMapping("/dept/updateById")
    public String updateById(@RequestBody Dept dept){
        return deptClientService.updateById(dept);
    }
    @GetMapping("/dept/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id){
        return deptClientService.deleteById(id);
    }
}
