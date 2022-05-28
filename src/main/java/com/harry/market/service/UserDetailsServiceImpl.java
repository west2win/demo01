package com.harry.market.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HarryGao
 */
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        //根据用户名查询用户权限
        com.harry.market.entity.User user = userService.getUser(username);
        String perm = user.getPerm();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        //声明用户授权
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(perm);
        grantedAuthorities.add(grantedAuthority);
        return new User(user.getUsername(), user.getPassword(),
                true, true, true, true, grantedAuthorities);
    }


}

