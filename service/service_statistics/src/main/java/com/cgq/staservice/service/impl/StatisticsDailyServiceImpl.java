package com.cgq.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgq.commonutils.R;
import com.cgq.staservice.client.UcenterClient;
import com.cgq.staservice.entity.StatisticsDaily;
import com.cgq.staservice.mapper.StatisticsDailyMapper;
import com.cgq.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;


    @Override
    public Integer registerCount(String day) {

        //先删除可能已经存在的当天日期查询记录，使得数据库只存在一条每天的记录
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StatisticsDaily::getDateCalculated,day);
        baseMapper.delete(queryWrapper);


        R data = ucenterClient.countRegister(day);
      Integer count = (Integer) data.getData().get("countRegister");

        //数据添加数据库
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(count);  //统计人数
        statisticsDaily.setDateCalculated(day);  //统计日期


        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setCourseNum(RandomUtils.nextInt(100,200));

        baseMapper.insert(statisticsDaily);

        return count;
    }

    //图表数据显示
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.lambda().between(StatisticsDaily::getDateCalculated,begin,end);
        wrapper.select(type,"date_calculated");  //只查一个字段

        List<StatisticsDaily> statisticsDailies = baseMapper.selectList(wrapper);

        //返回json数组结构,对应后端list
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        //遍历list集合封装
        for (StatisticsDaily statisticsDaily : statisticsDailies) {
            //封装日期集合
            date_calculatedList.add(statisticsDaily.getDateCalculated());
            //封装数量集合
            switch (type) {
                case "register_num":
                    dataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(statisticsDaily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(statisticsDaily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(statisticsDaily.getCourseNum());
                    break;
                default:
                    break;
            }

        }
        //封装在map中
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",dataList);
        return map;
    }
}
