package com.kz.health.dao;

import com.kz.health.pojo.User;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-13 20:47
 */
public interface UserDao {
    User findByUsername(String username);
}
