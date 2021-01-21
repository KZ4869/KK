package com.kz.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.constant.MessageConstant;
import com.kz.health.constant.RedisMessageConstant;
import com.kz.health.entity.Result;
import com.kz.health.pojo.Member;
import com.kz.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-11 09:45
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登陆验证
     * @param paramMap
     * @return
     */
    @PostMapping("/check")
    public Result check(@RequestBody Map<String,String> paramMap, HttpServletResponse res){

        //验证码校验
        Jedis jedis = jedisPool.getResource();
        //获取手机号
        String telephone = paramMap.get("telephone");
        //拼接验证码的key
        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;
        //redis中的验证码
        String codeInRedis = jedis.get(key);
        //判断是否为空
        if(StringUtils.isEmpty(codeInRedis)){
            //没有
            return new Result(false, "请重新获取验证码!");
        }
        //前端传递验证码 是否一致
        String validateCode = paramMap.get("validateCode");
        if(!codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //移除redis中的验证码 防止重复提交
        //jedis.del(key); 实际开发需要
        jedis.close();

        //会员判断
        Member member = memberService.findByTelephone(telephone);
        if(member == null){
            //添加会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //跟踪用户行为
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(30*24*60*60);//30天
        cookie.setPath("/");
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
