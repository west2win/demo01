package com.harry.market.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.entity.Order;
import com.harry.market.entity.User;
import com.harry.market.mapper.OrderMapper;
import com.harry.market.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    private OrderMapper orderMapper;

    public void deal(BigInteger goodId, int buyNum) {
        //springSecurity获取当前用户
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
        } else {
            String username = principal.toString();
        }

        //TODO

        orderMapper.newOrder();
        orderMapper.newUserOder();
        orderMapper.deal(goodId,buyNum);
    }


}
