package com.cgq.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.EduTeacher;
import com.cgq.eduservice.entity.vo.TeacherQuery;
import com.cgq.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-10
 */
@CrossOrigin //解决跨域
@RestController
@RequestMapping("/eduservice/eduTeacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

   //1.查询所有教室数据
    @GetMapping ("/findAll")
    public R getAll(){

        List<EduTeacher> teachers = teacherService.list();
        return R.ok().data("items",teachers);
    }

    @DeleteMapping("/{id}")
    public R removeTeacher(@PathVariable String id){

        boolean b = teacherService.removeById(id);
        return (b == true ? R.ok() : R.error());
    }

    //分页查询
    @GetMapping("/pageInfo/{current}/{limit}")
    public R pageTeacher(@PathVariable Long current,
                         @PathVariable Long limit){

        IPage<EduTeacher> Ipage = new Page<>(current,limit);
        IPage<EduTeacher> page = teacherService.page(Ipage);
        return R.ok().data("teachers",page.getRecords());

    }

    @PostMapping("/pageCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){

        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        Integer level = teacherQuery.getLevel();
        String name = teacherQuery.getName();

        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        if( StringUtils.isNotBlank(name)){
            queryWrapper.lambda().like(EduTeacher::getName,name);
        }
        if( StringUtils.isNotBlank(begin)){
            queryWrapper.lambda().ge(EduTeacher::getGmtCreate,begin);
        }
        if( StringUtils.isNotBlank(end)){
            queryWrapper.lambda().le(EduTeacher::getGmtModified,end);
        }
        if( StringUtils.checkValNotNull(level)){
            queryWrapper.lambda().eq(EduTeacher::getLevel,level);
        }

        //排序
      queryWrapper.lambda().orderByDesc(EduTeacher::getGmtModified);

        IPage<EduTeacher> Ipage = new Page<>(current,limit);
        IPage<EduTeacher> page = teacherService.page(Ipage,queryWrapper);


        return R.ok().data("teachers",page.getRecords()).data("total",page.getTotal());
    }

    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = teacherService.save(eduTeacher);
        if(b == true){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @GetMapping("getTeacher/{id}")
    public R getTeacherById(@PathVariable String id){

//        try {
//            int i = 1 / 0;
//        }catch (Exception e){
//            //执行自定义异常
//            throw new  GuliException(20001,"执行自定义异常");
//        }
        EduTeacher teacher = teacherService.getById(id);

        return R.ok().data("teacher",teacher);
    }

    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){

        boolean b = teacherService.updateById(eduTeacher);
        return b==true?R.ok():R.error();
    }
}

