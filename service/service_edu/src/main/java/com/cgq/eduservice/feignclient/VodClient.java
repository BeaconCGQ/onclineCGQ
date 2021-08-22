package com.cgq.eduservice.feignclient;

import com.cgq.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-vod",fallback = VodClientFallback.class)
public interface VodClient {

    @DeleteMapping("/eduvod/video/removeAliVideo/{id}")
    public R removeAliVideo(@PathVariable("id") String id);

    //删除多个视频
    @DeleteMapping("/eduvod/video/deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
