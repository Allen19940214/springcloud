package com.yuan.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TTLDirectExchangeConfig {
    //路由模式+延时队列
    @Bean
    public DirectExchange ttlDirectExchange(){
        return new DirectExchange("ttlDirectExchange",true,false);
    }
    @Bean
    public Queue ttlSmsQueue(){
        //设置过期时间 整个队列范围生效 +绑定死信交换机和死信队列
        Map<String, Object> map = new HashMap<>();
        map.put("x-message-ttl",10000);
        map.put("x-dead-letter-exchange","deadDirectExchange");
        map.put("x-dead-letter-routing-key","dead");
        //设置优先级，发消息时指定优先级，高的可先被消费，允许0-255 设置范围太大消耗机器性能 一般根据业务适当设置
        map.put("x-max-priority",10);
        return new Queue("ttlSmsQueue",true,false,false,map);
    }
    @Bean
    public Binding bindingSmsTTL(Queue ttlSmsQueue, DirectExchange ttlDirectExchange){
        return BindingBuilder.bind(ttlSmsQueue).to(ttlDirectExchange).with("ttlsms");
    }
}
