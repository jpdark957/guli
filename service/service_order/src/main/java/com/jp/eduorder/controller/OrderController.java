package com.jp.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jp.commonutils.JwtUtils;
import com.jp.commonutils.R;
import com.jp.eduorder.entity.Order;
import com.jp.eduorder.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-15
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
@Api(tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("生成订单")
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request) {
        //创建订单，返回订单号
        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("order", orderNo);
    }

    @ApiOperation("根据订单id查询订单信息")
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    @ApiOperation("根据课程id和用户id查询订单表中订单状态")
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);    //支付状态 1代表已经支付
        int count = orderService.count(wrapper);
        if (count > 0) {    //已经支付
            return true;
        } else {
            return false;
        }
    }
}

