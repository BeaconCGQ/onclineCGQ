package com.cgq.eduservice.feignclient;

import com.cgq.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodClientFallback implements VodClient{
    @Override
    public R removeAliVideo(String id) {
        System.out.println("服务调用removeAliVideo方法出现异常.....");
        return R.error().data("20001","服务调用removeAliVideo方法出现异常.....");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        System.out.println("服务调用deleteBatch方法出现异常.....");
        return R.error().data("20001","服务调用deleteBatch方法出现异常.....");
    }
}
