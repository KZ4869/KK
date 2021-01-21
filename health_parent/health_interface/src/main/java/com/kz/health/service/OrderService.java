package com.kz.health.service;

import com.kz.health.exception.HealthException;
import com.kz.health.pojo.Order;

import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-10 16:21
 */
public interface OrderService {
    //提交预约
    Order submitOrder(Map<String, String> paraMap) throws HealthException;
    //通过订单id查询预约信息
    Map<String, Object> findOrderDetailById(int id);
}
