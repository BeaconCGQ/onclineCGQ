package com.cgq.educenter.service;

import com.cgq.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgq.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-18
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdUser(String openid);

    Integer countRegister(String day);
}
