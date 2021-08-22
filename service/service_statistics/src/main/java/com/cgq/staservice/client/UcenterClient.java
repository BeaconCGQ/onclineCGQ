package com.cgq.staservice.client;

import com.cgq.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
public interface UcenterClient {

    //查询某一天注册人数
    @GetMapping("/educenter/ucenterMember/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day);
}
