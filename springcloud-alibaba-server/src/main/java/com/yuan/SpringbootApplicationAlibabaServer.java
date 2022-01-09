package com.yuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringbootApplicationAlibabaServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplicationAlibabaServer.class,args);
    }
}
