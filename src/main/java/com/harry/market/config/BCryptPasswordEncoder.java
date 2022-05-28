package com.harry.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @usage: 将SpringSecurity的加密器注册到bean
 */

@Configuration
public class BCryptPasswordEncoder {

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
