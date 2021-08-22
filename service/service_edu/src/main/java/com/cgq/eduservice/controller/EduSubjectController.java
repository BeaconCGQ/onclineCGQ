package com.cgq.eduservice.controller;


import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.subject.OneSubject;
import com.cgq.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
@CrossOrigin
@RestController
@RequestMapping("/eduservice/eduSubject")
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传的文件，内容读取

    @PostMapping("/addSubject/getAllSubject")
    public R addSubject(MultipartFile file){


        subjectService.saveSubject(file,subjectService);

        return R.ok();
    }

    //课程分类列表（树形）
    @GetMapping("/getAllSubject")
    public R getAllSubject(){

      List<OneSubject> list = subjectService.getAllSubject();  //泛型是一级分类，包含二级分类

        return R.ok().data("subjects",list);
    }

}

