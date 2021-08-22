package com.cgq.eduorder.client;

import com.cgq.commonutils.vo.CourseWebVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("service-edu")
public interface OrderCourseClient {

    //order服务使用获取课程信息
    @PostMapping("/eduservice/indexfront/getOrderCourseInfo/{courseId}")
    public CourseWebVo getOrderCourseInfo(@PathVariable("courseId") String courseId);
}
