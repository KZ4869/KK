package com.kz.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.pojo.Permission;
import com.kz.health.pojo.Role;
import com.kz.health.pojo.User;
import com.kz.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-13 20:36
 */
@Component
public class springSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询信息
        User user = userService.findByUsername(username);
        //用户的权限集合
        List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
       if(null != user){
           // 能查询到，就要授权，且返回UserDetails
           GrantedAuthority authority = null;
           //登录用户所拥有的角色集合
           Set<Role> roles = user.getRoles();
           //判断角色是否为空
           if(roles != null){
               //遍历所有的角色
               for (Role role : roles) {
                   //添加至集合
                   authority= new SimpleGrantedAuthority(role.getKeyword());
                   authorityList.add(authority);
                   //遍历角色 将角色下的权限也存入  角色和权限都有,那么无论是role还是hasAuthority 都能使用
                   Set<Permission> permissions = role.getPermissions();
                   if(permissions != null){
                       for (Permission permission : permissions) {
                           //添加权限
                           authority = new SimpleGrantedAuthority(permission.getKeyword());
                           authorityList.add(authority);
                       }
                   }
               }
           }
           //返回登录用户信息给security，会保存到session
           return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorityList);
       }
        return null;
    }
}
