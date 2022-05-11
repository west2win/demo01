package com.harry.market.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.controller.vo.OrderVO;
import com.harry.market.entity.BuyerMsg;
import com.harry.market.entity.Order;
import com.harry.market.mapper.BuyerMsgMapper;
import com.harry.market.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private BuyerMsgMapper buyerMsgMapper;

    public List<OrderVO> getInfoByUserId(Long userId) {
        return orderMapper.getOrderInfoByUserId(userId);
    }

    public Integer getOrderNumByUserId(Long userId) {
        return orderMapper.getOrderNumByUserId(userId);
    }

    public BuyerMsg getBuyerMsg(Long userId) {
        return buyerMsgMapper.selectById(userId);
    }

    public void modifyBuyerMsg(BuyerMsg buyerMsg) {
        buyerMsgMapper.updateById(buyerMsg);
    }

    public OrderVO getOrdermsgByOrderId(Long orderId) {
        return orderMapper.getOrderInfoByOrderId(orderId);
    }



}
