package com.yuan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.User;
import com.yuan.service.UserMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @Autowired
    UserMapperService userMapperService;
    @Autowired
    ObjectMapper objectMapper;
    @RequestMapping("/test")
    public String test(){
        return userMapperService.list().toString();
    }
    @PostMapping("/testPage")
    public String testPage(@RequestParam("page")Integer page,@RequestParam("num") Integer num) throws JsonProcessingException {
        Page<User> UserPage = new Page<>(page,num);
        Page<User> page1= userMapperService.page(UserPage);
        String s = objectMapper.writeValueAsString(page1);
        return s;
    }
}
