package com.kz.health.dao;

import com.github.pagehelper.Page;
import com.kz.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-30 21:32
 */
public interface SetmealDao {
    void add(Setmeal setmeal);

    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);


    Page<Setmeal> findByCondition(String queryString);
    //获取套餐根据id
    Setmeal findById(int id);
    //获取套餐和检查组关系组
    List<Integer> findCheckgroupIdsBySetmealId(int id);
    //修改套餐
    void update(Setmeal setmeal);
    //根据id删除检查组和套餐关系
    void deleteSetmealCheckGroup(Integer setmealId);

    //判断是否被订单使用
    int findOrderCountBySetmealId(int id);
    //根据id删除套餐
    void deleteById(int id);

    List<String> findImgs();

    List<Setmeal> findAll();

    Setmeal findDetailById(int id);
    //套餐预约查询
    List<Map<String, Object>> findSetmealCount();
}
