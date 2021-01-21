package com.kz.health.exception;

/**
 * @Description: 自定义异常
 * 区分系统异常
 * 友好提示
 * 终止已经不符合业务逻辑的代码
 * @Author: KZ4869
 * @CreateTime: 2020-12-26 00:29
 */
public class HealthException extends RuntimeException{
    public HealthException(String message) {
        super(message);
    }
}
