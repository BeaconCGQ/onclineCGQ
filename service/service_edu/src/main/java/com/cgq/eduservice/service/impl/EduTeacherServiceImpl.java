package com.cgq.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.eduservice.entity.EduCourse;
import com.cgq.eduservice.entity.EduTeacher;
import com.cgq.eduservice.mapper.EduTeacherMapper;
import com.cgq.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-10
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Autowired
    private EduTeacherMapper teacherMapper;

//    @Cacheable(key = "'teacher'",value = "teacherList")
    @Override
    public List<EduTeacher> getHotTeacher() {

        QueryWrapper<EduTeacher> qwCourse = new QueryWrapper<>();
        qwCourse.lambda().orderByDesc(EduTeacher::getId);
        qwCourse.last("limit 4");

        List<EduTeacher> teachers = teacherMapper.selectList(qwCourse);

        return teachers;

    }

    @Override
    public Map<String, Object> getTeacherPage(Page<EduTeacher> teacherPage) {

        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(EduTeacher::getId);

        Page<EduTeacher> pageParam = teacherMapper.selectPage(teacherPage, queryWrapper);

        List<EduTeacher> records = pageParam.getRecords();
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
}
