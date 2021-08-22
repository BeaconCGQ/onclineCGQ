package com.cgq.educenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.cgq"})
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.cgq.educenter.mapper")
public class CenterApplication {

    public static void main(String[] args) {

        SpringApplication.run(CenterApplication.class,args);
    }
}
