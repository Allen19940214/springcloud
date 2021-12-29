package com.yuan.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {
    //路由模式
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("orderDirectExchange",true,false);
    }
    @Bean
    public Queue smsQueue(){
        return new Queue("smsQueue");
    }
    @Bean
    public Queue emailQueue(){
        return new Queue("emailQueue");
    }
    @Bean
    public Queue weChatQueue(){
        return new Queue("weChatQueue");
    }
    @Bean
    public Binding bindingSms(Queue smsQueue, DirectExchange directExchange){
        return BindingBuilder.bind(smsQueue).to(directExchange).with("sms");
    }
    @Bean
    public Binding bindingEmail(Queue emailQueue,DirectExchange directExchange){
        return BindingBuilder.bind(emailQueue).to(directExchange).with("email");
    }
    @Bean
    public Binding bindingWeChat(Queue weChatQueue,DirectExchange directExchange){
        return BindingBuilder.bind(weChatQueue).to(directExchange).with("wechat");
    }
}
