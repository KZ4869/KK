package com.kz.health.dao;

import com.kz.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void add(Order order);
    List<Order> findByCondition(Order order);
    Map findById4Detail(Integer id);
    Integer findOrderCountByDate(String date);
    Integer findOrderCountAfterDate(String date);
    Integer findVisitsCountByDate(String date);
    Integer findVisitsCountAfterDate(String date);
    List<Map<String,Object>> findHotSetmeal();

    /**
     * 统计两个日期间的预约数
     * @param startDate
     * @param endDate
     * @return
     */
    int findOrderCountBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
        }
