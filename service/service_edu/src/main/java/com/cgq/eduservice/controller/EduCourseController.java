package com.cgq.eduservice.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.EduCourse;
import com.cgq.eduservice.entity.vo.CourseInfoVo;
import com.cgq.eduservice.entity.vo.CoursePublishVo;
import com.cgq.eduservice.entity.vo.CourseQuery;
import com.cgq.eduservice.entity.vo.TeacherQuery;
import com.cgq.eduservice.feignclient.VodClient;
import com.cgq.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
@CrossOrigin
@RestController
@RequestMapping("/eduservice/eduCourse")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private VodClient vodClient;

    //添加课程基本信息
    @PostMapping("/addCourseInfo")
    public R addCourse(@RequestBody CourseInfoVo courseInfoVo) {

        String id = courseService.saveCourseInfo(courseInfoVo);
        //然后返回课程id，便于页面第二步添加大纲，因为后两步是在这个课程基础上的

        return R.ok().data("courseId", id);
    }

    //根据课程id查询课程信息
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {

        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);

        return R.ok().data("CourseInfoVo", courseInfoVo);
    }

    //修改课程信息
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {

        courseService.updateCourseInfo(courseInfoVo);

        return R.ok();

    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
       CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);

        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    //发布课程（修改课程状态）
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){

        EduCourse eduCourse  = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);

        return R.ok();
    }

    //课程列表 TODO 完善条件查询带分页
    @PostMapping("getCourseList/{current}/{limit}")
    public R getCourseList(@PathVariable Long current,
                           @PathVariable Long limit,
                           @RequestBody(required = false) CourseQuery courseQuery){


        IPage<EduCourse> courses = courseService.QueryCourse(current,limit,courseQuery);

        return R.ok().data("courseList",courses.getRecords()).data("total",courses.getTotal());

    }

    //删除课程
    @DeleteMapping("{courseId}")
    @SentinelResource(value = "deleteCourse")
    public R deleteCourse(@PathVariable String courseId){


        courseService.removeCourse(courseId);

        return R.ok();
    }

}

