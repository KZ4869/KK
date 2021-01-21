package com.kz.health.service;

import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-30 18:09
 */
public interface SetmealService {
    /**
     * 添加检查套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findById(int id);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 修改
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 删除套餐
     * @param id
     */
    void deleteById(int id) throws HealthException;

    List<String> findImgs();

    /**
     * 查询套餐数据
     * @return
     */
    List<Setmeal> findAll();

    Setmeal findDetailById(int id);

    List<Map<String, Object>> findSetmealCount();
}
