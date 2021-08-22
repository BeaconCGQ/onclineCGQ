package com.cgq.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-10
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> getHotTeacher();

    Map<String, Object> getTeacherPage(Page<EduTeacher> teacherPage);
}
