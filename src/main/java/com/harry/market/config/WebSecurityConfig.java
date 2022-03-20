package com.harry.market.config;

import com.harry.market.handler.CustomizeAuthenticationEntryPoint;
import com.harry.market.handler.CustomizeAuthenticationFailureHandler;
import com.harry.market.handler.CustomizeAuthenticationSuccessHandler;
import com.harry.market.handler.CustomizeLogoutSuccessHandler;
import com.harry.market.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    CustomizeAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    CustomizeLogoutSuccessHandler logoutSuccessHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 临时放行所有接口
        // 开发时临时放行
        web.ignoring().antMatchers("/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置认证方式等
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http相关的配置，包括登入登出、异常处理、会话管理等
//        super.configure(http);
        //放行swagger相关接口
        http
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/*").permitAll()
                .antMatchers("/csrf").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
        //放行注册接口
        http
                .authorizeRequests()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/login").permitAll()
                .anyRequest().authenticated();
        http.cors().and().exceptionHandling().
                //登入
                    and().formLogin().
                    permitAll().//允许所有用户
                    successHandler(authenticationSuccessHandler).//登录成功处理逻辑
                    failureHandler(authenticationFailureHandler).//登录失败处理逻辑
                //登出
                    and().logout().
                    permitAll().//允许所有用户
                    logoutSuccessHandler(logoutSuccessHandler).//登出成功处理逻辑
                    deleteCookies("JSESSIONID");//登出之后删除cookie
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取用户账号密码及权限信息
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }


}

