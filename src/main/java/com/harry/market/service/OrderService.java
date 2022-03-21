package com.harry.market.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.entity.Order;
import com.harry.market.entity.User;
import com.harry.market.mapper.OrderMapper;
import com.harry.market.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    private OrderMapper orderMapper;



}
