package com.cgq.eduorder.service;

import com.cgq.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);

    Map<String, String> getOrderStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);
}
