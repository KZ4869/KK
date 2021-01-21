package com.kz.health.service;

import com.kz.health.exception.HealthException;
import com.kz.health.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-05 21:34
 */
public interface OrderSettingService {
    //添加预约日期和最大预约数
    void add(ArrayList<OrderSetting> orderSettings) throws HealthException;

    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    void editNumberByDate(OrderSetting orderSetting) throws HealthException;
}
