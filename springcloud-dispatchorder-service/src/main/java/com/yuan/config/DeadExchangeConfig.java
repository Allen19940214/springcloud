package com.yuan.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DeadExchangeConfig {
    //路由模式+死信交换机+死信队列
    @Bean
    public DirectExchange deadDirectExchange(){
        return new DirectExchange("deadDirectExchange",true,false);
    }
    @Bean
    public Queue deadSmsQueue(){
        return new Queue("deadSmsQueue",true,false,false);
    }
    @Bean
    public Binding bindingSmsDead(Queue deadSmsQueue, DirectExchange deadDirectExchange){
        return BindingBuilder.bind(deadSmsQueue).to(deadDirectExchange).with("dead");
    }
}
