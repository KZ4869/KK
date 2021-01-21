package com.kz.health.controller;

import com.kz.health.constant.MessageConstant;
import com.kz.health.constant.RedisMessageConstant;
import com.kz.health.entity.Result;
import com.kz.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: 用于校验验证码
 * @Author: KZ4869
 * @CreateTime: 2021-01-10 12:50
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //用于将验证码存储到redis中
        Jedis jedis = jedisPool.getResource();
        //先从redis中看是否存在
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        String codeInRedis = jedis.get(key);
        //说明有值
        if (null != codeInRedis) {
            //不为空 发送过了
            return new Result(false, "验证码已经发送过了，请注意查收");
        }
        //没有发送过
        //生成验证码
        //生成六位随机数
        Integer integer = ValidateCodeUtils.generateValidateCode(6);
        String validateCode = integer + "";
        try {
            //发送短信 由于阿里云需要企业资质 就用控制台输出 填写代替

            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            //存入redis 验证码存储时间不宜过长 设置为10分钟
            jedis.setex(key, 10 * 60, validateCode);
            jedis.close();
            System.out.println(key + ":" + validateCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);

    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //用于将验证码存储到redis中
        Jedis jedis = jedisPool.getResource();
        //先从redis中看是否存在 拼接字符串
        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;
        String codeInRedis = jedis.get(key);
        //说明有值
        if (null != codeInRedis) {
            //不为空 发送过了
            return new Result(false, "验证码已经发送过了，请注意查收");
        }
        //没有发送过
        //生成验证码
        //生成六位随机数
        Integer integer = ValidateCodeUtils.generateValidateCode(6);
        String validateCode = integer + "";
        try {
            //发送短信 由于阿里云需要企业资质 就用控制台输出 填写代替

            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            //存入redis 验证码存储时间不宜过长 设置为10分钟
            jedis.setex(key, 10 * 60, validateCode);
            jedis.close();
            System.out.println(key + ":" + validateCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);

    }


}
