package com.cgq.eduservice.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.EduVideo;
import com.cgq.eduservice.feignclient.VodClient;
import com.cgq.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-12
 */
@CrossOrigin
@RestController
@RequestMapping("/eduservice/eduVideo")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){

        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节 TODO:删除小节也要删除视频
    @DeleteMapping("{id}")
    @SentinelResource(value = "deleteVideo")
    public R deleteVideo(@PathVariable String id){

        //通过小节id获取视频id，调用方法删除
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();

        if(StringUtils.checkValNotNull(videoSourceId)) {

            vodClient.removeAliVideo(videoSourceId);
        }
        videoService.removeById(id);

        return R.ok();
    }

    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){

        videoService.updateById(eduVideo);
        return R.ok();
    }

    //按id查询小节
    @GetMapping("getVideo/{id}")
    public R getVideo(@PathVariable String id){

        EduVideo video = videoService.getById(id);
        return R.ok().data("video",video);
    }

}

