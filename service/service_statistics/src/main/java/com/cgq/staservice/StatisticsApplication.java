package com.cgq.staservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //定时任务
@ComponentScan(basePackages = {"com.cgq"})
@SpringBootApplication
@MapperScan("com.cgq.staservice.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class StatisticsApplication {

    public static void main(String[] args) {

        SpringApplication.run(StatisticsApplication.class,args);
    }
}
