package com.harry.market.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.controller.dto.OrderVO;
import com.harry.market.entity.Order;
import com.harry.market.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderVO> getInfoByUserId(Long userId) {
        return orderMapper.getOrderInfoByUserId(userId);
    }

    public Integer getOrderNumByUserId(Long userId) {
        return orderMapper.getOrderNumByUserId(userId);
    }



}
