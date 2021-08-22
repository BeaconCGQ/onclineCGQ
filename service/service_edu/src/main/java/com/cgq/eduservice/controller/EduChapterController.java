package com.cgq.eduservice.controller;


import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.EduChapter;
import com.cgq.eduservice.entity.chapter.ChapterVo;
import com.cgq.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
@CrossOrigin
@RestController
@RequestMapping("/eduservice/eduChapter")
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //课程大纲列表,根据课程id查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){

       List<ChapterVo> list = chapterService.getChapterVideoById(courseId);

        return R.ok().data("allChapterVideo",list);
    }

    //添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){

        chapterService.save(eduChapter);
        return R.ok();
    }
    //按id查询章节
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){

        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }
     //修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){

        chapterService.updateById(eduChapter);
        return R.ok();
    }
    //删除章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){

        chapterService.deleteChapter(chapterId);

        return R.ok();
    }
}

