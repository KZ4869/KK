package com.kz.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kz.health.dao.OrderSettingDao;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.OrderSetting;
import com.kz.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-05 21:37
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    /**
     * 批量导入
     */
    @Transactional
    public void add(ArrayList<OrderSetting> orderSettings) throws HealthException {
        //遍历
        for (OrderSetting orderSetting : orderSettings) {
            //判断是否存在 通过日期来查询 注意:日期是否有时分秒,数据库中没有时分秒
            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if (null != osInDB) {
                //数据库中有数据 判断已预约人数是否大于可预约人数
                if (osInDB.getReservations() > orderSetting.getNumber()) {
                    throw new HealthException(orderSetting.getOrderDate() + "中的已预约人数大于可预约人数");
                }
                orderSettingDao.updateNumber(orderSetting);
            } else {
                //不存在
                orderSettingDao.add(orderSetting);
            }
        }

    }

    /**
     * 根据日期获取预约设置信息
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        //拼接
        Map<String, String> map = new HashMap<String, String>();
        map.put("startDate", month + "-01");
        map.put("endDate", month + "-31");
        return orderSettingDao.getOrderSettingByMonth(map);
    }

    /**
     * 预约设置
     *
     * @param orderSetting
     */
    @Override
    @Transactional
    public void editNumberByDate(OrderSetting orderSetting) throws HealthException{

        //判断是否存在 通过日期来查询 注意:日期是否有时分秒,数据库中没有时分秒
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //存在
        if (null != osInDB) {
            //数据库中有数据 判断已预约人数是否大于可预约人数
            if (osInDB.getReservations() > orderSetting.getNumber()) {
                throw new HealthException(orderSetting.getOrderDate() + "中的已预约人数大于可预约人数");
            }
            orderSettingDao.updateNumber(orderSetting);
        } else {
            //不存在
            orderSettingDao.add(orderSetting);
        }


    }


}

