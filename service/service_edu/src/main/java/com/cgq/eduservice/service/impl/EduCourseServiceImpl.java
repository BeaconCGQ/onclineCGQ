package com.cgq.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.eduservice.entity.EduCourse;
import com.cgq.eduservice.entity.EduCourseDescription;
import com.cgq.eduservice.entity.EduTeacher;
import com.cgq.eduservice.entity.EduVideo;
import com.cgq.eduservice.entity.front.CourseFrontVo;
import com.cgq.eduservice.entity.front.CourseWebVo;
import com.cgq.eduservice.entity.vo.CourseInfoVo;
import com.cgq.eduservice.entity.vo.CoursePublishVo;
import com.cgq.eduservice.entity.vo.CourseQuery;
import com.cgq.eduservice.feignclient.VodClient;
import com.cgq.eduservice.mapper.EduCourseMapper;
import com.cgq.eduservice.service.EduChapterService;
import com.cgq.eduservice.service.EduCourseDescriptionService;
import com.cgq.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgq.eduservice.service.EduVideoService;
import com.cgq.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        //向课程表添加课程基本信息
        //把courseInfoVo转换为EduCourse
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = courseMapper.insert(eduCourse);

        if(insert == 0){
            throw new GuliException(20001,"添加课程失败");
        }

        //向课程简介表添加课程简介
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(eduCourse.getId());
        courseDescriptionService.save(courseDescription);


        return eduCourse.getId();
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        EduCourse eduCourse = courseMapper.selectById(courseId);
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(courseId);

        CourseInfoVo courseInfoVo = new CourseInfoVo();

        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {

        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);

        int i = courseMapper.updateById(eduCourse);
        if(i == 0){
            throw new GuliException(20001,"修改课程失败");
        }


        //修改描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);

        courseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {

        CoursePublishVo publishCourseInfo = courseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //条件查询课程列表
    @Override
    public IPage<EduCourse> QueryCourse(Long current, Long limit, CourseQuery courseQuery) {

        String courseName = courseQuery.getCourseName();
        String courseStatus = courseQuery.getCourseStatus();

        QueryWrapper<EduCourse> qwCourse = new QueryWrapper<>();
        if(StringUtils.checkValNotNull(courseName)){
            qwCourse.lambda().like(EduCourse::getTitle,courseName);
        }
        if(StringUtils.checkValNotNull(courseStatus)){
            qwCourse.lambda().eq(EduCourse::getStatus,courseStatus);
        }

        qwCourse.lambda().orderByDesc(EduCourse::getGmtModified);

        IPage<EduCourse> Ipage = new Page<>(current,limit);
        IPage<EduCourse> courseList = courseMapper.selectPage(Ipage, qwCourse);

        return courseList;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {

        QueryWrapper queryWrapper = new QueryWrapper();

        //0.删除课程下所有视频
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("video_source_id");
        List eduVideoList = videoService.list(queryWrapper);
        //List<EduVideo>变为List<String>
        List<String> videoIds = new ArrayList<>();

        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = (EduVideo) eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(StringUtils.checkValNotNull(videoSourceId)){

                videoIds.add(videoSourceId);
            }
        }
//根据视频id删除多个视频
        if(videoIds.size() > 0){

            vodClient.deleteBatch(videoIds);
        }

        queryWrapper.clear();

        //1.根据课程id删除小节
        queryWrapper.eq("course_id",courseId);

        videoService.remove(queryWrapper);

        //2.根据课程id删除章节
        chapterService.remove(queryWrapper);

        queryWrapper.clear();
        //3.根据课程id删除描述
        queryWrapper.eq("id",courseId);
        courseDescriptionService.remove(queryWrapper);

        //4.根据课程id删除课程本身
        courseMapper.deleteById(courseId);
    }

//    @Cacheable(key = "'course'" ,value = "courseList")
    @Override
    public  List<EduCourse> getHotCourse() {

        QueryWrapper<EduCourse> qwCourse = new QueryWrapper<>();
        qwCourse.lambda().orderByDesc(EduCourse::getId);
        qwCourse.last("limit 8");

        List<EduCourse> eduCourses = courseMapper.selectList(qwCourse);

        return eduCourses;

    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo) {

        QueryWrapper<EduCourse> queryWrapper  = new QueryWrapper<>();
        if(StringUtils.checkValNotNull(courseFrontVo.getSubjectParentId())){//一级分类id

            queryWrapper.lambda().eq(EduCourse::getSubjectParentId,courseFrontVo.getSubjectParentId());
        }

        if(StringUtils.checkValNotNull(courseFrontVo.getSubjectId())){//二级分类id

            queryWrapper.lambda().eq(EduCourse::getSubjectId,courseFrontVo.getSubjectId());
        }

        if(StringUtils.checkValNotNull(courseFrontVo.getBuyCountSort())){//关注度排序

            queryWrapper.lambda().orderByDesc(EduCourse::getBuyCount);
        }
        if(StringUtils.checkValNotNull(courseFrontVo.getGmtCreateSort())){//时间排序

            queryWrapper.lambda().orderByDesc(EduCourse::getGmtCreate);
        }
        if(StringUtils.checkValNotNull(courseFrontVo.getPriceSort())){//价格排序

            queryWrapper.lambda().orderByDesc(EduCourse::getPrice);
        }


        Page<EduCourse> pageParam = courseMapper.selectPage(coursePage, queryWrapper);


        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {


        return courseMapper.getBaseCourseInfo(courseId);
    }
}
