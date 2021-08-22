package com.cgq.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgq.educms.entity.CrmBanner;
import com.cgq.educms.mapper.CrmBannerMapper;
import com.cgq.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-17
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Autowired
    private CrmBannerMapper bannerMapper;

//    @Cacheable(key = "'selectIndexList'",value = "banner")
    @Override
    public List<CrmBanner> getAllBanners() {

        QueryWrapper<CrmBanner> qwBanner = new QueryWrapper<>();
        qwBanner.lambda().orderByDesc(CrmBanner::getId);
        //last拼接sql语句
        qwBanner.last("limit 2");

        List<CrmBanner> banners = bannerMapper.selectList(qwBanner);
        return banners;
    }
}
