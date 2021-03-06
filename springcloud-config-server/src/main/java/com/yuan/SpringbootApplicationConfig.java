package com.yuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class SpringbootApplicationConfig {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplicationConfig.class,args);
    }
}
