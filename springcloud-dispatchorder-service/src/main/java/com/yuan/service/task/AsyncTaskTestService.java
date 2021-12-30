package com.yuan.service.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@EnableAsync//开启支持异步任务
public class AsyncTaskTestService {

    @Async//标注异步方法
    public String asyncTask() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return "正在处理数据";
    }
}
