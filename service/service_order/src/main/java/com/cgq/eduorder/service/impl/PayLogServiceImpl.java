package com.cgq.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgq.eduorder.entity.Order;
import com.cgq.eduorder.entity.PayLog;
import com.cgq.eduorder.mapper.PayLogMapper;
import com.cgq.eduorder.service.OrderService;
import com.cgq.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgq.eduorder.utils.HttpClient;
import com.cgq.servicebase.exceptionhandler.GuliException;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayLogMapper payLogMappery;

    @Override
    public Map createNative(String orderNo) {

         try {
             //1根据订单号查询订单
             QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
             queryWrapper.lambda().eq(Order::getOrderNo,orderNo);
             Order order = orderService.getOne(queryWrapper);

             //2.使用map设置生成二维码需要参数

             Map m = new HashMap();
             //1、设置支付参数
             m.put("appid", "wx74862e0dfcf69954");
             m.put("mch_id", "1558950191");
             m.put("nonce_str", WXPayUtil.generateNonceStr());
             m.put("body", order.getCourseTitle());
             m.put("out_trade_no", orderNo);
             m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
             m.put("spbill_create_ip", "127.0.0.1");
             m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
             m.put("trade_type", "NATIVE");

             //3.发送httpclient请求，传递参数xml格式，微信支付提供固定地址
             HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

                //设置参数
             client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
             client.setHttps(true);
             //执行
             client.post();

             //4.得到发送请求返回结果
             String xml = client.getContent();

             //xml转换map集合
             Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);


             Map map = new HashMap<>();
             map.put("out_trade_no", orderNo);
             map.put("course_id", order.getCourseId());
             map.put("total_fee", order.getTotalFee());
             map.put("result_code", resultMap.get("result_code"));  //返回二维码操作状态码
             map.put("code_url", resultMap.get("code_url"));    //二维码地址

             return map;

         }catch (Exception e){
             throw new GuliException(20001,"生成二维码失败");
         }


    }

    @Override
    public Map<String, String> getOrderStatus(String orderNo) {

        try {


            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2.发送http请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            return resultMap;

        }catch (Exception e){
            return null;
        }

    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {

        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        //更新状态
        if(order.getStatus().intValue() == 1){
            return;
        }
        order.setStatus(1);
        orderService.updateById(order);

        //支付表添加支付记录
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());   //订单完成时间
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)

        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id")); //流水号
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }

}
