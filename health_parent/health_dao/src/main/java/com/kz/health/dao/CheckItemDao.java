package com.kz.health.dao;

import com.github.pagehelper.Page;
import com.kz.health.pojo.CheckItem;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckItemDao {
    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

    /**
     * 判断该id在关系表中是否存在 存在不能删除
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(int id);

    /**
     * 根据id查询
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
