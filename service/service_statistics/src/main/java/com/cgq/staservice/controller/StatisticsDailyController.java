package com.cgq.staservice.controller;


import com.cgq.commonutils.R;
import com.cgq.staservice.client.UcenterClient;
import com.cgq.staservice.entity.StatisticsDaily;
import com.cgq.staservice.service.StatisticsDailyService;
import org.apache.commons.lang3.RandomUtils;
import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
@CrossOrigin
@RestController
@RequestMapping("/staservice/statistics")
public class StatisticsDailyController {


    @Autowired
    private StatisticsDailyService statisticsService;

    //统计某一天注册人数
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){

       Integer registerCount = statisticsService.registerCount(day);

        return R.ok().data("registerCount",registerCount);

    }

    //图表显示，返回日期json数组、数量json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,
                      @PathVariable String begin,
                      @PathVariable String end){

       Map<String,Object> map = statisticsService.getShowData(type,begin,end);

        return R.ok().data(map);
    }

}

