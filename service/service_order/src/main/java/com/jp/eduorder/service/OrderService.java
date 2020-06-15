package com.jp.eduorder.service;

import com.jp.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-15
 */
public interface OrderService extends IService<Order> {

    //生成订单
    String createOrders(String courseId, String memberIdByJwtToken);
}
