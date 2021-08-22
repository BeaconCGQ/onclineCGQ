package com.cgq.educms.controller;


import com.cgq.commonutils.R;
import com.cgq.educms.entity.CrmBanner;
import com.cgq.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * 前台banner显示
 * </p>
 *
 * @author cgq
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    //查询所有Banner，并按id降序取前八条数据
    @GetMapping("getAllBanners")
    public R getALlBanner(){

        List<CrmBanner> banners = bannerService.getAllBanners();

        return R.ok().data("banners",banners);
    }
}

