package com.cgq.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan(basePackages = {"com.cgq"})//加载公共配置类
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EduApplication {

    public static void main(String[] args) {

        SpringApplication.run(EduApplication.class,args);
    }
}
