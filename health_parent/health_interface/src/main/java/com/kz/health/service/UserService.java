package com.kz.health.service;

import com.kz.health.pojo.User;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-13 20:44
 */
public interface UserService {
    User findByUsername(String username);
}
