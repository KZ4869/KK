package com.kz.health.dao;

import com.kz.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-05 21:42
 */
public interface OrderSettingDao {
    //通过日期判断是否有值
    OrderSetting findByOrderDate(Date orderDate);
    //修改原有的预约表中预约人数
    void updateNumber(OrderSetting orderSetting);
    //空白添加预约
    void add(OrderSetting orderSetting);
    //根据月份查找预约信息
    List<Map<String, Integer>> getOrderSettingByMonth(Map<String, String> map);
    /**
     * 更新已预约人数
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);

    long findCountByOrderDate(Date orderDate);
}
