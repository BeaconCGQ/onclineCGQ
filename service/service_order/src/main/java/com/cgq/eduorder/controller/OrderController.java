package com.cgq.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgq.commonutils.JwtUtils;
import com.cgq.commonutils.R;
import com.cgq.eduorder.entity.Order;
import com.cgq.eduorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
@RestController
@CrossOrigin
@RequestMapping("/eduorder/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //生成订单
    @PostMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){


       String orderNo = orderService.createOrder(courseId,request);

        return R.ok().data("orderNo",orderNo);
    }

    //查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getOrderNo,orderId);

        Order order = orderService.getOne(queryWrapper);

        return R.ok().data("order",order);
    }

    //根据课程id和用户id查询课程支付状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,
                               @PathVariable String memberId){

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Order::getCourseId,courseId);
        wrapper.lambda().eq(Order::getMemberId,memberId);
        wrapper.lambda().eq(Order::getStatus,1);
        int count = orderService.count(wrapper);

        return count > 0;

    }

}

