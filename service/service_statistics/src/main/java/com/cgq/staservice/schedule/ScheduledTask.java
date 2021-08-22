package com.cgq.staservice.schedule;

import com.cgq.staservice.service.StatisticsDailyService;
import com.cgq.staservice.utlis.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;


//    @Scheduled(cron = "0/5 * * * * ?") //每隔五秒执行一次，只需要启动服务
//    public void task1(){
//
//        System.out.println("task1...........");
//    }


    //每天凌晨一点查询前一天数据添加数据库
    @Scheduled(cron = "0 0 1 * * ?") //每隔五秒执行一次，只需要启动服务
    public void task2(){

        dailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }
}
