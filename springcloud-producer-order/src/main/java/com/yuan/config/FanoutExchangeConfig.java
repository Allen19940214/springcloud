package com.yuan.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutExchangeConfig {
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("orderFanoutExchange");
    }
    @Bean
    public Queue queue1(){
        return new Queue("FanoutQueue1");
    }
    @Bean
    public Queue queue2(){
        return new Queue("FanoutQueue2");
    }
    @Bean
    public Binding binding1(Queue queue1,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }
    @Bean
    public Binding binding2(Queue queue2,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }
}