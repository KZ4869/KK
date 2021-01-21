package com.kz.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-23 17:15
 */
public class ServiceApplication {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:applicationContext-service.xml");
        //产生死循环
        while (true){}
    }
}
