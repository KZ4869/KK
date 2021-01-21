package com.kz.health.service;

import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.CheckGroup;

import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-27 23:38
 */
public interface CheckGroupService {
    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);
    //根据id查检查组
    CheckGroup findById(int groupId);
    //根据id查 检查项
    List<Integer> findCheckItemIdsByCheckGroupId(int groupId);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);
    //根据id删除
    void deleteById(int id)throws HealthException;

    List<CheckGroup> findAll();
}
