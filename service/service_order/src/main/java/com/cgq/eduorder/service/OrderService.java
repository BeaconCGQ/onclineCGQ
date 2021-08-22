package com.cgq.eduorder.service;

import com.cgq.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
public interface OrderService extends IService<Order> {

    String createOrder(String courseId, HttpServletRequest request);
}
