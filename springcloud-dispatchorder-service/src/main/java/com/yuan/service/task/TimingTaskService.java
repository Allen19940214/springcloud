package com.yuan.service.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling//开启定时任务
public class TimingTaskService {

    //对本地消息冗余表中发送失败的消息 进行重新发送
    @Scheduled(cron = "0 0 1 * * ?")
    public void outputLog(){
        System.out.println("正在执行定时任务");
    }
}
