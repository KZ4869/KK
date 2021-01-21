package com.kz.health.service;

import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.CheckItem;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckItemService {
    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();

    /**
     *
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(int id) throws HealthException;

    /**
     * 根据id回显数据
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 修改
     * @param checkItem
     */
    void update(CheckItem checkItem);
}
