package com.cgq.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.commonutils.JwtUtils;
import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.EduCourse;
import com.cgq.eduservice.entity.EduTeacher;
import com.cgq.eduservice.entity.chapter.ChapterVo;
import com.cgq.eduservice.entity.front.CourseFrontVo;
import com.cgq.eduservice.entity.front.CourseWebVo;
import com.cgq.eduservice.feignclient.OrderClient;
import com.cgq.eduservice.service.EduChapterService;
import com.cgq.eduservice.service.EduCourseService;
import com.cgq.eduservice.service.EduTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("/eduservice/indexfront")
@RestController
@CrossOrigin
public class EduCourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    //查询前8条热门课程
    @GetMapping("index")
    public R index(){

       List<EduCourse> courseHot = courseService.getHotCourse();
       List<EduTeacher> teacherHot =  teacherService.getHotTeacher();

       return R.ok().data("courseHot",courseHot).data("teacherHot",teacherHot);
    }

    //条件查询分页
    @PostMapping("getFrontCourses/{page}/{limit}")
    public R getFrontCourses(@PathVariable Long page,
                             @PathVariable Long limit,
                             @RequestBody(required = false) CourseFrontVo courseFrontVo){

        Page<EduCourse> coursePage = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(coursePage,courseFrontVo);

        return R.ok().data("courseMap",map);
    }

    //课程详情
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourse(@PathVariable String courseId, HttpServletRequest request){

        //查询基本信息
      CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        //查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoById(courseId);

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //根据课程id和用户id查询当前课程是否被购买
        boolean buyCourse = orderClient.isBuyCourse(courseId, memberId);

        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuyCourse",buyCourse);
    }

    //order服务使用获取课程信息
    @PostMapping("getOrderCourseInfo/{courseId}")
    public CourseWebVo getOrderCourseInfo(@PathVariable String courseId){

        CourseWebVo course = courseService.getBaseCourseInfo(courseId);
        CourseWebVo courseOrder = new CourseWebVo();
        BeanUtils.copyProperties(course,courseOrder);

        return courseOrder;
    }

}
