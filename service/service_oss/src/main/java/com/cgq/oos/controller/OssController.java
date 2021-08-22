package com.cgq.oos.controller;

import com.cgq.commonutils.R;
import com.cgq.oos.service.ossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RequestMapping("/eduoss/fileoss")
@RestController
public class OssController {


    @Autowired
    private ossService ossService;

    //上传头像方法
    @PostMapping
    public R uploadFile(MultipartFile file){

        //获取上传文件
       String url = ossService.uploadFileAvatar(file);

        return R.ok().data("url",url);
    }
}
