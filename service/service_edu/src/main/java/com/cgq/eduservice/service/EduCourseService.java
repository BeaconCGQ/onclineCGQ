package com.cgq.eduservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgq.eduservice.entity.EduTeacher;
import com.cgq.eduservice.entity.front.CourseFrontVo;
import com.cgq.eduservice.entity.front.CourseWebVo;
import com.cgq.eduservice.entity.vo.CourseInfoVo;
import com.cgq.eduservice.entity.vo.CoursePublishVo;
import com.cgq.eduservice.entity.vo.CourseQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    IPage<EduCourse> QueryCourse(Long current, Long limit, CourseQuery courseQuery);

    void removeCourse(String courseId);

    List<EduCourse> getHotCourse();

    Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);

}
