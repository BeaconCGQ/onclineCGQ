package com.cgq.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgq.eduservice.entity.EduSubject;
import com.cgq.eduservice.entity.excel.SubjectData;
import com.cgq.eduservice.entity.subject.OneSubject;
import com.cgq.eduservice.entity.subject.TwoSubject;
import com.cgq.eduservice.listener.SubjectListener;
import com.cgq.eduservice.mapper.EduSubjectMapper;
import com.cgq.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Autowired
    private EduSubjectMapper subjectMapper;

    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {

        try {
            //输入流
            InputStream is = file.getInputStream();
            EasyExcel.read(is, SubjectData.class, new SubjectListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //分类 树形
    @Override
    public List<OneSubject> getAllSubject() {

        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        //1.查询所以一级分类 (parent_id = 0 )
        queryWrapper.lambda().eq(EduSubject::getParentId,"0");
        List<EduSubject> eduSubjects1 = subjectMapper.selectList(queryWrapper);

        queryWrapper.clear();

        //2.查询所有二级分类
        queryWrapper.lambda().ne(EduSubject::getParentId,"0");
        List<EduSubject> eduSubjects2 = subjectMapper.selectList(queryWrapper);


        //创建list封装最终数据
        List<OneSubject> subjectList = new ArrayList<>();

        //3.封装一级分类
        for (EduSubject eduSubject : eduSubjects1) {

            OneSubject oneSubject = new OneSubject();
            //把oneSubject值取出来放到EduSubject
            BeanUtils.copyProperties(eduSubject,oneSubject);
            subjectList.add(oneSubject);

            //封装二级分类
            List<TwoSubject> twoSubjectList = new ArrayList<>();
            for (EduSubject eduSubject2 : eduSubjects2) {

                if(eduSubject2.getParentId().equals(eduSubject.getId())){

                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(eduSubject2,twoSubject);
                    twoSubjectList.add(twoSubject);
                }
            }

            oneSubject.setChildren(twoSubjectList);

        }
        //4.封装二级分类

        return subjectList;
    }
}
