package com.cgq.educenter.mapper;

import com.cgq.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author cgq
 * @since 2021-08-18
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer countRegisterDay(String day);
}
