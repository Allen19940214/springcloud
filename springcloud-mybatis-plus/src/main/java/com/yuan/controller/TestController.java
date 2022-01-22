package com.yuan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.User;
import com.yuan.service.UserMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.activation.DataSource;


@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    UserMapperService userMapperService;
    @Autowired
    ObjectMapper objectMapper;
    @RequestMapping("/test1")
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
    @PostMapping("/testLogin")
    public String login(@RequestParam("username")String name,@RequestParam("password") String password) throws JsonProcessingException {
        User login = userMapperService.login(name, password);
        return objectMapper.writeValueAsString(login);
    }
    @PostMapping("/update")
    public String update(@RequestBody User user) throws JsonProcessingException {
        boolean update = userMapperService.update(user, null);
        return objectMapper.writeValueAsString(update);
    }
}
