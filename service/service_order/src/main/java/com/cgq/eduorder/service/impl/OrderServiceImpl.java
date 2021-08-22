package com.cgq.eduorder.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cgq.commonutils.JwtUtils;
import com.cgq.commonutils.vo.CourseWebVo;
import com.cgq.commonutils.vo.UcenterMember;
import com.cgq.eduorder.client.OrderCourseClient;
import com.cgq.eduorder.client.OrderUserClient;
import com.cgq.eduorder.entity.Order;
import com.cgq.eduorder.mapper.OrderMapper;
import com.cgq.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgq.eduorder.utils.OrderNoUtil;
import com.cgq.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderUserClient orderUserClient;

    @Autowired
    private OrderCourseClient orderCourseClient;

    @Override
    public String createOrder(String courseId, HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.checkValNull(memberId)){
            throw new GuliException(20001,"请先登录");
        }
        //远程调用获取用户和课程信息
        CourseWebVo orderCourseInfo = orderCourseClient.getOrderCourseInfo(courseId);
        UcenterMember orderUserInfo = orderUserClient.getCommentUser(memberId);

        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
        order.setCourseId(courseId);
        order.setCourseTitle(orderCourseInfo.getTitle());
        order.setCourseCover(orderCourseInfo.getCover());
        order.setTeacherName("test");
        order.setTotalFee(orderCourseInfo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(orderUserInfo.getMobile());
        order.setNickname(orderUserInfo.getNickname());

        order.setStatus(0); //支付状态
        order.setPayType(1);  //支付类型 1 微信

        orderMapper.insert(order);

        return order.getOrderNo();
    }
}
