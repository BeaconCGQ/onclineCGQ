package com.cgq.msmservice.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cgq.commonutils.R;
import com.cgq.msmservice.service.MsmService;
import com.cgq.msmservice.utils.TXMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {


    @Override
    public void sendPhone(String phone, Map<String, String> param) {

        String code1 = param.get("code");
       TXMessageUtils.sendMessage(phone,code1);
    }
}
