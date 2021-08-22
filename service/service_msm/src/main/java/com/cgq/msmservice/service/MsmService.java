package com.cgq.msmservice.service;

import com.cgq.commonutils.R;

import java.util.Map;

public interface MsmService {

    void sendPhone(String phone, Map<String, String> param);

}
