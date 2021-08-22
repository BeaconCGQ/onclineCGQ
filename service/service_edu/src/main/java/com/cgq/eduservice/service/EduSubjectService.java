package com.cgq.eduservice.service;

import com.cgq.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgq.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    List<OneSubject> getAllSubject();
}
