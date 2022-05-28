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

/**
 * @author 222100209_李炎东
 */
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

    /**
     * @author 222100209_李炎东
     * @usage 获取某用户所有的订单信息
     * @param userId 用户Id
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    public List<OrderVO> getInfoByUserId(Long userId, Integer pageNum, Integer pageSize) {
        return orderMapper.getOrderInfoByUserId(userId,pageNum*pageSize,pageSize);
    }

    /**
     * @author 222100209_李炎东
     * @usgae 获取某用户订单数量
     * @param userId
     * @return
     */
    public Integer getOrderNumByUserId(Long userId) {
        return orderMapper.getOrderNumByUserId(userId);
    }

    /**
     * @author 222100209_李炎东
     * @usage 获取买家信息
     * @param userId 买家Id
     * @return
     */
    public BuyerMsg getBuyerMsg(Long userId) {
        return buyerMsgMapper.selectById(userId);
    }

    /**
     * @author 222100209_李炎东
     * @usage 修改买家信息
     * @param buyerMsg 新的买家信息
     */
    public void modifyBuyerMsg(BuyerMsg buyerMsg) {
        buyerMsgMapper.updateById(buyerMsg);
    }

    /**
     * @author 222100209_李炎东
     * @usgae 通过订单Id获取订单信息
     * @param orderId 订单Id
     * @return
     */
    public OrderVO getOrdermsgByOrderId(Long orderId) {
        return orderMapper.getOrderInfoByOrderId(orderId);
    }

    /**
     * @author 222100209_李炎东
     * @usgae 分类获取订单信息
     * @param buyer_id 买家Id
     * @param status 筛选参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    public List<OrderVO> getInfoBySort(Long buyer_id,Integer status, Integer pageNum, Integer pageSize) {
        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("buyer_id",buyer_id);
        List<UserOrder> uos = userOrderMapper.selectList(wrapper);
        ArrayList<OrderVO> orderVOS = new ArrayList<>();
        if (uos.isEmpty()) {
            return null;
        }
        for (UserOrder uo : uos) {
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            if (status==0) {
                orderQueryWrapper.eq("status",status);
//                sta = "待收货";
            } else if (status==1) {
                orderQueryWrapper.eq("status",status);
//                sta = "已完成";
            }
//        orderQueryWrapper.eq("buyer_id",buyer_id);

            orderQueryWrapper.last("limit "+pageNum*pageSize+","+pageSize);
            orderQueryWrapper.eq("id",uo.getId());
            Order order = orderMapper.selectOne(orderQueryWrapper);
//            Order order = orderMapper.selectById(uo.getId());
            if (order==null) {
                continue;
            }
            OrderVO o = new OrderVO();
            o.setOrderId(order.getId().toString());
            ItemVO item = goodService.getItemById(order.getItem_id());
            if (order.isStatus()) {
                o.setStatus("已完成");
            } else {
                o.setStatus("待收货");
            }
//            o.setSellerHead(item.getSellerHead());
            o.setItemId(item.getId());
            o.setNumber(item.getNumber());
            o.setSellerName(item.getSellerName());
            o.setItemName(item.getName());
            o.setItemPhoto(item.getPhoto());
            o.setPrice(item.getPrice());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            o.setDate(sdf.format(order.getGmt_modified()));
            orderVOS.add(o);
        }
        return orderVOS;
    }

    /**
     * @author 222100209_李炎东
     * @usage 获取分类的订单信息数量
     * @param buyer_id 买家Id
     * @param status 筛选参数
     * @return
     */
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
