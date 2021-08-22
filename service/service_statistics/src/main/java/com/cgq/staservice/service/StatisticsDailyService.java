package com.cgq.staservice.service;

import com.cgq.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    Integer registerCount(String day);

    Map<String, Object> getShowData(String type, String begin, String end);
}
