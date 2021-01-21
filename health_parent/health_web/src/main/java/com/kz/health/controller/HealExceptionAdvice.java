package com.kz.health.controller;

import com.kz.health.entity.Result;
import com.kz.health.exception.HealthException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description: 全局异常处理
 * Advice 通知 切面
 * @Author: KZ4869
 * @CreateTime: 2020-12-27 20:21
 */
@RestControllerAdvice
public class HealExceptionAdvice {

    private static final Logger log = (Logger) LoggerFactory.getLogger(Exception.class);

    /**
     * ExceptionHandler 用来捕获指定类型的异常
     * @param e
     * @return
     */
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException e){
        //输出日志信息
        log.error("发生HealthException异常",e);
        //反馈给前端
        return new Result(false, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生异常!",e);
        return new Result(false, "操作异常,请联系管理员!");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e){
        return new Result(false, "权限不足");
    }

}
