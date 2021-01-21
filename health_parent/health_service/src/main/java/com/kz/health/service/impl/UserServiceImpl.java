package com.kz.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kz.health.dao.UserDao;
import com.kz.health.pojo.User;
import com.kz.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-13 20:46
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 通过用户名查找用户信息 包括权限和角色
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);

    }
}
