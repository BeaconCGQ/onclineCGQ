package com.cgq.eduorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("service-ucenter")
public interface OrderUserClient {

    //根据用户id获取用户信息
    @PostMapping("/educenter/ucenterMember/getOrderUserInfo/{memberId}")
    public com.cgq.commonutils.vo.UcenterMember getCommentUser(@PathVariable("memberId") String memberId);
}
