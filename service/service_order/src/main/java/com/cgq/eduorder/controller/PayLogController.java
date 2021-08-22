package com.cgq.eduorder.controller;


import com.alibaba.csp.sentinel.adapter.reactor.SentinelReactorTransformer;
import com.cgq.commonutils.R;
import com.cgq.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
@RestController
@CrossOrigin
@RequestMapping("/eduorder/payLog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成微信支付二维码接口
    @GetMapping("createdNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){

        //返回包含二维码和其他信息
       Map map = payLogService.createNative(orderNo);
        System.out.println("二维码：" + map);
       return R.ok().data(map);
    }

    //查询订单支付状态
    @GetMapping("getOrderStatus/{orderNo}")
    public R getOrderStatus(@PathVariable String orderNo){

      Map<String,String> map = payLogService.getOrderStatus(orderNo);
    if(map == null){
        return R.error().message("支付出错");
    }
    if(map.get("trade_state").equals("SUCCESS")){
        //添加记录到支付表，更新订单状态
        payLogService.updateOrderStatus(map);
        return R.ok().message("支付成功");
    }

    return R.ok().code(25000).message("支付中");

}
}

