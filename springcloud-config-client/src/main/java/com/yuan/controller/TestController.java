package com.yuan.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {
    @Value("${mybatis.configuration.cache-enabled}")
    private String msg;
    @Value("${server.port}")
    private String port;
    @RequestMapping("/getMsg")
    public String getMsg(){
        return msg+port;
    }
}
