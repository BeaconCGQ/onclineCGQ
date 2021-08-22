package com.cgq.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgq.eduservice.entity.EduSubject;
import com.cgq.eduservice.entity.excel.SubjectData;
import com.cgq.eduservice.service.EduSubjectService;
import com.cgq.servicebase.exceptionhandler.GuliException;

public class SubjectListener extends AnalysisEventListener<SubjectData> {

    //此类不能交给spring管理，需要自己new，不能注入其他对象

    public EduSubjectService eduSubjectService;

    public SubjectListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    public SubjectListener() {
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {

        if(subjectData == null){
            throw new GuliException(20001,"文件内容为空");
        }

        //按行读取，第一个值一级分类，第二个值二级分类
        EduSubject eduSubject = existOne(subjectData.getOneSubjectName(), eduSubjectService);
        if(eduSubject == null){//数据库不存在，添加到数据库
            eduSubject = new EduSubject();
            eduSubject.setParentId("0");
            eduSubject.setTitle(subjectData.getOneSubjectName());
            eduSubjectService.save(eduSubject);

        }

        //获取一级分类id值作为parent_id
        String pid = eduSubject.getId();
        //判断与添加二级分类
        EduSubject twoSubject = existTwo(subjectData.getTwoSubjectName(), eduSubjectService,pid);
        if(twoSubject == null){//数据库不存在，添加到数据库
            twoSubject = new EduSubject();
            twoSubject.setParentId(pid);
            twoSubject.setTitle(subjectData.getTwoSubjectName());
            eduSubjectService.save(twoSubject);

        }
    }

    //判断一级分类不能重复添加
    public EduSubject existOne(String name,EduSubjectService eduSubjectService){

        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EduSubject::getTitle,name).eq(EduSubject::getParentId,"0");

        EduSubject subject = eduSubjectService.getOne(queryWrapper);
        return subject;
    }

    //判断二级分类不能重复添加
    public EduSubject existTwo(String name,EduSubjectService eduSubjectService,String pid){

        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EduSubject::getTitle,name).eq(EduSubject::getParentId,pid);

        EduSubject subject = eduSubjectService.getOne(queryWrapper);
        return subject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
