package com.cgq.eduservice.service;

import com.cgq.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgq.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoById(String courseId);

    void deleteChapter(String chapterId);
}
