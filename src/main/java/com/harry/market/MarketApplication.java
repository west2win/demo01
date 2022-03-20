package com.harry.market;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@EnableAutoConfiguration(exclude = {
//        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
//})
@MapperScan("com.harry.market.mapper")
public class MarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }

}
