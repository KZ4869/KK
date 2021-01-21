package com.kz.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kz.health.dao.CheckItemDao;
import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.CheckItem;
import com.kz.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-23 16:47
 * interfaceClass 发布到zookeeper上的接口名
 */
//发布服务
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 新增
     *
     * @param checkItem
     */
    @Override
    @Transactional
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        // 使用静态方法，pageheleper
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //有条件 实现模糊查询 拼接%%
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //不为空 添加模糊
            //拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //紧接着的查询语句会被分页
        //page 是pageHelper的对象 分页信息
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //包装到PageResult 在返回
        //解耦
        //total为基本类型 在序列化中会丢失
        //page对象内容太多
        return new PageResult<CheckItem>(page.getTotal(),page.getResult());
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException{
        // 判断检查项是否被检查组使用了 通过聚合函数
        int count = checkItemDao.findCountByCheckItemId(id);
        if(count>0){
        //报错
            throw new HealthException("该检查项已经被使用了，不能删除!");
        }
        checkItemDao.deleteById(id);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    /**
     * 修改
     * @param checkItem
     */
    @Override
    @Transactional
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
