package com.cgq.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.EduCourse;
import com.cgq.eduservice.entity.EduTeacher;
import com.cgq.eduservice.service.EduCourseService;
import com.cgq.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
@RestController
public class EduTeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //分页查询
    @PostMapping("getTeacherFrontPage/{page}/{limit}")
    public R getTeacherFrontPage(@PathVariable Long page,
                                 @PathVariable Long limit){

        Page<EduTeacher> teacherPage = new Page<>(page,limit);
        Map<String,Object> teacherMap = teacherService.getTeacherPage(teacherPage);

        //返回分页所有数据
        return R.ok().data("teacherMap",teacherMap);
    }

    //根据讲师id查询讲师详情
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){

        //查询基本信息
        EduTeacher teacherInfo = teacherService.getById(teacherId);

        //查询课程信息
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EduCourse::getTeacherId,teacherId);
        List<EduCourse> courses = courseService.list(queryWrapper);

        return R.ok().data("teacherInfo",teacherInfo).data("courses",courses);
    }
}
