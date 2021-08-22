package com.cgq.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.cgq.commonutils.R;
import com.cgq.servicebase.exceptionhandler.GuliException;
import com.cgq.vod.service.VodService;
import com.cgq.vod.utils.ConstantVodUtils;
import com.cgq.vod.utils.InitObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RequestMapping("/eduvod/video")
@RestController
public class VodController {

    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){

        //返回视频id
       String videoId = vodService.uploadVideo(file);

       return R.ok().data("videoId",videoId);
    }

    //根据视频id删除视频
    @DeleteMapping("removeAliVideo/{id}")
    public R removeAliVideo(@PathVariable String id){

        try {
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
        return R.ok();
    }

    //删除课程是删除多个视频
    @DeleteMapping("deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){

        vodService.removeMoreAlyVideo(videoIdList);

        return R.ok();
    }

    //根据视频id获取播放凭证
    @GetMapping("getPlayerAuth/{id}")
    public R getPlayerAuth(@PathVariable String id){

        try {
            //创建初始化对象
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //设置视频id
            request.setVideoId(id);

            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            String playAuth = response.getPlayAuth();

            return R.ok().data("playAuth",playAuth);

        }catch (Exception e){
            throw new GuliException(20001,"获取凭证失败");
        }
    }
}
