package com.kz.health.controller;

import com.kz.health.constant.MessageConstant;
import com.kz.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-15 00:11
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getLoginUsername")
    public Result getLoginUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        //添加前一年的数据
        calendar.add(Calendar.YEAR, -1);
        //遍历到今天为止的前12个月
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            System.out.println(calendar.getTime());
        }
    }

}
