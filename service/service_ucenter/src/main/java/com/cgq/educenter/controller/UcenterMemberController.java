package com.cgq.educenter.controller;


import com.cgq.commonutils.JwtUtils;
import com.cgq.commonutils.R;
import com.cgq.educenter.entity.UcenterMember;
import com.cgq.educenter.entity.vo.RegisterVo;
import com.cgq.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-18
 */
@CrossOrigin
@RestController
@RequestMapping("/educenter/ucenterMember")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){

        //返回token值
       String token = ucenterMemberService.login(member);

       return R.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){

        ucenterMemberService.register(registerVo);


        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request){

        String UserId = JwtUtils.getMemberIdByJwtToken(request);
        //根据userId获取用户信息
        UcenterMember User = ucenterMemberService.getById(UserId);

        return R.ok().data("User",User);
    }

    //根据token字符串获取用户信息
    @PostMapping("getInfoUc/{id}")
    public com.cgq.commonutils.vo.UcenterMember getInfo(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        com.cgq.commonutils.vo.UcenterMember memeber = new com.cgq.commonutils.vo.UcenterMember();
        BeanUtils.copyProperties(ucenterMember,memeber);
        return memeber;
    }

    //根据用户id获取用户信息
    @PostMapping("getOrderUserInfo/{memberId}")
    public com.cgq.commonutils.vo.UcenterMember getCommentUser(@PathVariable String memberId){

        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        com.cgq.commonutils.vo.UcenterMember member = new com.cgq.commonutils.vo.UcenterMember();
        BeanUtils.copyProperties(ucenterMember,member);

        return member;
    }

    //查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){

      Integer count = ucenterMemberService.countRegister(day);

      return R.ok().data("countRegister",count);
    }
}

