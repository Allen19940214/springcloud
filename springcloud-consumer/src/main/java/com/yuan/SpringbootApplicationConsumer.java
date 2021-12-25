package com.yuan;

import com.yuan.config.RibbonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
//@RibbonClient(name ="SPRINGCLOUD-PRODUCER-DEPT",configuration = RibbonConfig.class)
public class SpringbootApplicationConsumer {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplicationConsumer.class,args);
    }
}
