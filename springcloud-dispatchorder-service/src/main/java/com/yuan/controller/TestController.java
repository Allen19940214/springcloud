package com.yuan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TestController {
    @RequestMapping("/hello2")
    public ModelAndView hello() {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
