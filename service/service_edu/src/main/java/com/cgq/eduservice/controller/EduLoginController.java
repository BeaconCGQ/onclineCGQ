package com.cgq.eduservice.controller;

import com.cgq.commonutils.R;
import org.springframework.web.bind.annotation.*;

@CrossOrigin //解决跨域
@RequestMapping("/eduservice/user")
@RestController
public class EduLoginController {

    //login
    @PostMapping("/login")
    public R login(){


        return R.ok().data("token","admin");
    }

    //info
    @GetMapping("/info")
    public R info(){

        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","http://m.imeitou.com/uploads/allimg/2021061715/ldwlukms3iq.jpg");
    }
}
