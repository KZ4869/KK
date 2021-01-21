package com.kz.security;

import com.kz.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-13 16:19
 */
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库中查询
        User user = findByUsername(username);
        //security 需要的登录用户信息
        //用户名 密码 权限集合
        List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
        //添加角色 GrantedAuthority是接口 添加的是实现类
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorityList.add(authority);
        authority = new SimpleGrantedAuthority("ABC");
        authorityList.add(authority);
        org.springframework.security.core.userdetails.User securityUser = new
                //"{bcrypt}" 必须制定用什么加密 或者xml配置
                org.springframework.security.core.userdetails.User(username,user.getPassword() , authorityList);

        return securityUser;
    }

    /**
     * 假设数据库
     */
    private User findByUsername (String username){
        if("admin".equals(username)) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("$2a$10$m9J7UX0wl7Q2Y.EZRJVWt.OYf0UTHbwOmDV6qL13GsAkO5cSTFWpW");
            return user;
        }
        return null;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("1234");
        boolean matches = encoder.matches("1234", "$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a");
        System.out.println(matches);

        System.out.println(encode);
    }

}
