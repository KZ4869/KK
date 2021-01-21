package com.kz.health.dao;

import com.github.pagehelper.Page;
import com.kz.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-27 23:53
 */
public interface CheckGroupDao {
    //添加检查组
    void add(CheckGroup checkGroup);
    //建立检查组和检查项的联系
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);
    //条件查询
    Page<CheckGroup> findByCondition(String queryString);
    //根据id查找检查组
    CheckGroup findById(int groupId);
    //根据id查该组的检查项
    List<Integer> findCheckItemIdsByCheckGroupId(int groupId);
    //修改检查组
    void update(CheckGroup checkGroup);
    //删除检查组和检查项的关系 根据检查组id
    void deleteCheckGroupCheckItem(Integer id);
    //检查是否与套餐有关系
    int findSetmealCountByCheckGroupId(int id);
    //没有关系删除检查组
    void deleteById(int id);

    List<CheckGroup> findAll();
}
