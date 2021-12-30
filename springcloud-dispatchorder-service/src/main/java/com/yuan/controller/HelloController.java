package com.yuan.controller;

import com.yuan.service.task.AsyncTaskTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    AsyncTaskTestService asyncTaskTestService;

    //异步任务测试
    @RequestMapping("/task")
    public String TestAsyncTask() throws InterruptedException {
        asyncTaskTestService.asyncTask();
        System.out.println("6666");
        return "处理成功请稍等";
    }
}
