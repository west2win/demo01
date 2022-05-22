package com.harry.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.controller.vo.ItemVO;
import com.harry.market.controller.vo.OrderVO;
import com.harry.market.entity.BuyerMsg;
import com.harry.market.entity.Order;
import com.harry.market.entity.UserOrder;
import com.harry.market.mapper.BuyerMsgMapper;
import com.harry.market.mapper.OrderMapper;
import com.harry.market.mapper.UserOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private BuyerMsgMapper buyerMsgMapper;

    @Autowired
    private GoodService goodService;
    @Resource
    private UserOrderMapper userOrderMapper;

    public List<OrderVO> getInfoByUserId(Long userId, Integer pageNum, Integer pageSize) {
        return orderMapper.getOrderInfoByUserId(userId,pageNum,pageSize);
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

    public List<OrderVO> getInfoBySort(Long buyer_id,Integer status, Integer pageNum, Integer pageSize) {
        String sta;
        if (status==0) {
            sta = "待收货";
        } else {
            sta = "已完成";
        }

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
//        orderQueryWrapper.eq("buyer_id",buyer_id);
        orderQueryWrapper.eq("status",status);
        orderQueryWrapper.last("limit "+pageNum+","+pageSize);

        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("buyer_id",buyer_id);
        List<UserOrder> uos = userOrderMapper.selectList(wrapper);
        ArrayList<OrderVO> orderVOS = new ArrayList<>();
        if (uos.isEmpty()) {
            return null;
        }
        for (UserOrder uo : uos) {
            orderQueryWrapper.eq("id",uo.getId());
            Order order = orderMapper.selectOne(orderQueryWrapper);
//            Order order = orderMapper.selectById(uo.getId());
            if (order==null) {
                continue;
            }
            OrderVO o = new OrderVO();
            o.setOrderId(order.getId().toString());
            ItemVO item = goodService.getItemById(order.getItem_id());
//            o.setSellerHead(item.getSellerHead());
            o.setSellerName(item.getSellerName());
            o.setItemName(item.getName());
            o.setItemPhoto(item.getPhoto());
            o.setPrice(item.getPrice());
            o.setStatus(sta);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            o.setDate(sdf.format(order.getGmt_modified()));
            orderVOS.add(o);
        }
        return orderVOS;
    }

    public Long getCountBySort(Long buyer_id,Integer status) {
        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("buyer_id",buyer_id);
        List<UserOrder> uos = userOrderMapper.selectList(wrapper);
        long count = 0;
        QueryWrapper<Order> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("status",status);
        for (UserOrder uo : uos) {
            wrapper1.eq("id",uo.getId());
            count += orderMapper.selectCount(wrapper1);
        }
        return count;
    }


}
