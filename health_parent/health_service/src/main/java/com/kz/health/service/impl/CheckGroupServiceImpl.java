package com.kz.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kz.health.constant.MessageConstant;
import com.kz.health.dao.CheckGroupDao;
import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.CheckGroup;
import com.kz.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-27 23:50
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    //自动引入
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //添加检查组表单信息
        checkGroupDao.add(checkGroup);
        //获取检查组的id 通过selectKey
        Integer checkGroupId = checkGroup.getId();
        //判断勾选项是否为空
        if (checkitemIds != null) {
            //遍历选中的检查项id
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        //有查询条件的处理 模糊处理
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //拼接字符串
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //使用分页插件 传入当前页 和每页显示条数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //紧挨着的查询会分页
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<CheckGroup>(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询检查组
     *
     * @param groupId
     * @return
     */
    @Override
    public CheckGroup findById(int groupId) {
        return checkGroupDao.findById(groupId);
    }

    /**
     * 根据检查组id该id检查项
     *
     * @param groupId
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int groupId) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(groupId);
    }

    /**
     * 编辑检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组
        checkGroupDao.update(checkGroup);
        //判断勾选项是否为空
        if (checkitemIds != null) {
            //修改关系表 ①先删除 ②后添加
            checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
            //遍历选中的检查项id
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(), checkitemId);
            }
        }
    }

    /**
     * 根据id删除检查组
     * 抛出异常 接口也要
     * @param id
     * @return
     */
    @Override
    public void deleteById(int id) throws HealthException{
        //先判断检查组是否与套餐有联系
        int count = checkGroupDao.findSetmealCountByCheckGroupId(id);
        if (count > 0) {
            //被使用了 不能删除
            throw new HealthException("该检查组已经被套餐使用了，不能删除!");
        }
        /*
        没有使用 则可以删除
        先删除 检查组和检查项的关系
         */
        checkGroupDao.deleteCheckGroupCheckItem(id);

        //再删除检查组
        checkGroupDao.deleteById(id);

    }
    //查找所有的检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
