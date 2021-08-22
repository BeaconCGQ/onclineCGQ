package com.cgq.msmservice.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cgq.commonutils.R;
import com.cgq.msmservice.service.MsmService;
import com.cgq.msmservice.utils.RandomUtil;
import com.cgq.msmservice.utils.TXMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信
    @GetMapping("send/{phone}")
    public R sendPhone(@PathVariable String phone){

//        1.从redis去验证码，若获取失败则发送短信
        String code1 = redisTemplate.opsForValue().get(phone);
        if(StringUtils.checkValNotNull(code1)){
            return R.ok();
        }

        String code = RandomUtil.getSixBitRandom();
        Map<String,String> param = new HashMap<>();
        param.put("code",code);
         msmService.sendPhone(phone,param);
         redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);//5分钟失效

         return R.ok();

    }
}
