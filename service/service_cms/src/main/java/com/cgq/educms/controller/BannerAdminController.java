package com.cgq.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.commonutils.R;
import com.cgq.educms.entity.CrmBanner;
import com.cgq.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 首页banner表 前端控制器
 * 后台
 * </p>
 *
 * @author cgq
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //1.分页查询
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable Long page,
                        @PathVariable Long limit){

        Page<CrmBanner> bannerPage = new Page<>(page,limit);
        Page<CrmBanner> pageBanner = bannerService.page(bannerPage);

        return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }

    //添加Banner
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){

        bannerService.save(crmBanner);
        return R.ok();
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("getBanner/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("updateBanner")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("removeBanner/{id}")
    public R remove(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }
}


