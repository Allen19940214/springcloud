package com.yuan.config;

import com.yuan.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class MyTest {

    @Autowired
    @Qualifier("myService1")
    private MyService myService;

    /*public MyTest(MyService myService1) {
        super();
        this.myService = myService1;
    }
    @Autowired
    public MyTest(MyService myService2,MyService myService1) {
        super();
        this.myService = myService2;
    }*/
    /*@Bean
    public OrderServiceImpl orderServiceImpl1(){
        return new OrderServiceImpl();
    }
    @Bean
    public OrderServiceImpl orderServiceImpl2(){
        return new OrderServiceImpl();
    }
    @Bean
    public OrderServiceImpl orderServiceImpl3(){
        return new OrderServiceImpl();
    }*/
    @Bean
    public MyService myService1(){
        return new MyService();
    }
    @Bean()
    public MyService myService2(){
        return new MyService();
    }
}
