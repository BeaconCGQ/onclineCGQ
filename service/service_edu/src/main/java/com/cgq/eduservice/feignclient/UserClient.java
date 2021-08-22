package com.cgq.eduservice.feignclient;

import com.cgq.commonutils.vo.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-ucenter")
public interface UserClient {

    @PostMapping("/educenter/ucenterMember/getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable("id") String id) ;
}
