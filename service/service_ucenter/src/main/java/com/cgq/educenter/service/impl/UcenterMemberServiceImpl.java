package com.cgq.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cgq.commonutils.JwtUtils;
import com.cgq.commonutils.MD5;
import com.cgq.educenter.entity.UcenterMember;
import com.cgq.educenter.entity.vo.RegisterVo;
import com.cgq.educenter.mapper.UcenterMemberMapper;
import com.cgq.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgq.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-18
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private UcenterMemberMapper memberMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //登录
    @Override
    public String login(UcenterMember member) {

        //获取手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //非空判断
        if(StringUtils.checkValNull(mobile) || StringUtils.checkValNull(password)){
            throw new GuliException(20001,"手机号或密码为空，请重新输入");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UcenterMember::getMobile,mobile);
        UcenterMember ucenterMember = memberMapper.selectOne(queryWrapper);
        if(ucenterMember == null){
            throw new GuliException(20001,"手机号不存在，请输入正确账号");
        }

        //判断加密密码
        if(!MD5.encrypt(password).equals(ucenterMember.getPassword())){
            throw new GuliException(20001,"密码错误，请重新输入");
        }
        //判断用户是否被禁用
        if(ucenterMember.getIsDisabled()){
            throw new GuliException(20001,"该账号被封禁");
        }

        //登录成功生成token
        String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());


        return token;
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {

        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        if(StringUtils.checkValNull(code) || StringUtils.checkValNull(mobile) ||
           StringUtils.checkValNull(nickname) || StringUtils.checkValNull(password)){

            throw new GuliException(20001,"注册失败，不能为空");
        }

        //获取redis的code与输入code比较
        String redisCode = redisTemplate.opsForValue().get(mobile);

        if(!code.equals(redisCode)){
            throw new GuliException(20001,"验证码错误");
        }
        //手机号不能重复
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UcenterMember::getMobile,mobile);
        Integer count = memberMapper.selectCount(queryWrapper);

        if(count > 0){
            throw new GuliException(20001,"手机号已经存在");
        }
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("shanghai.aliyuncs.com/2021/08/12/e5b438aebcb34bbd9eb3d81b0d46e1c6file.png");

        memberMapper.insert(member);
    }

    //根据openid查询数据库是否存在
    @Override
    public UcenterMember getOpenIdUser(String openid) {

        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UcenterMember::getOpenid,openid);
        UcenterMember member = memberMapper.selectOne(queryWrapper);

        return member;
    }

    //查询某天注册人数
    @Override
    public Integer countRegister(String day) {


        return memberMapper.countRegisterDay(day);
    }
}
