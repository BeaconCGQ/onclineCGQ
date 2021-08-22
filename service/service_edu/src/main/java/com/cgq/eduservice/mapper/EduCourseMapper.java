package com.cgq.eduservice.mapper;

import com.cgq.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgq.eduservice.entity.front.CourseWebVo;
import com.cgq.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublishCourseInfo(String courseId);

    CourseWebVo getBaseCourseInfo(String courseId);
}
