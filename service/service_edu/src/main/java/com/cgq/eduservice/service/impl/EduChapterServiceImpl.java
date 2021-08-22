package com.cgq.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgq.eduservice.entity.EduChapter;
import com.cgq.eduservice.entity.EduVideo;
import com.cgq.eduservice.entity.chapter.ChapterVo;
import com.cgq.eduservice.entity.chapter.VideoVo;
import com.cgq.eduservice.mapper.EduChapterMapper;
import com.cgq.eduservice.mapper.EduVideoMapper;
import com.cgq.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgq.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduChapterMapper chapterMapper;

    @Autowired
    private EduVideoMapper videoMapper;

    //根据课程id查询课程章节和小节
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {

        //根据课程id查所有章节
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EduChapter::getCourseId,courseId);
        List<EduChapter> eduChapters = chapterMapper.selectList(queryWrapper);

        List<ChapterVo> finalChapter = new ArrayList<>();

        QueryWrapper<EduVideo> qwVideo = new QueryWrapper<>();
        for (EduChapter eduChapter : eduChapters) {
            //每个章节封装到Vo里
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);

            finalChapter.add(chapterVo);

            List<VideoVo> videoVos = new ArrayList<>();
            //根据每个章节id查所有小节
            qwVideo.lambda().eq(EduVideo::getChapterId,eduChapter.getId());
            List<EduVideo> eduVideos = videoMapper.selectList(qwVideo);
            qwVideo.clear();

            for (EduVideo eduVideo : eduVideos) {
                VideoVo videoVo = new VideoVo();
                BeanUtils.copyProperties(eduVideo,videoVo);
                videoVos.add(videoVo);
            }

            chapterVo.setChildren(videoVos);


        }

        return finalChapter;
    }

    //删除章节(如果旗下有小节，不让删除)
    @Override
    public void deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> qwVideo = new QueryWrapper<>();
        qwVideo.lambda().eq(EduVideo::getChapterId,chapterId);
        Integer count = videoMapper.selectCount(qwVideo);

        if(count == 0){
            chapterMapper.deleteById(chapterId);
        }else{
            throw new GuliException(20001,"章节下小节存在，无法删除");
        }
    }
}
