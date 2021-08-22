package com.cgq.educms.service;

import com.cgq.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-17
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> getAllBanners();

}
