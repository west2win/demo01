package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.controller.vo.OrderVO;
import com.harry.market.entity.Item;
import com.harry.market.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("Update item Set number = number - #{dealNumber} Where id = #{goodsId}")
    List<Item> deal(Long goodsId, Integer dealNumber);

    @Select("select seller_id from item where id = #{goodId}")
    String getSellerId(Long goodId);

    @Select("select `order`.id orderId,(select nickname from user_details where user.id=`user_order`.buyer_id limit 1) buyerName, (select head from user_details where user_details.id=`user_order`.buyer_id) buyerHead, (select nickname from user_details where user.id=`order`.seller_id limit 1) sellerName, (select head from user_details where user_details.id=`order`.seller_id) sellerHead, (select `name` from item where item.id=`order`.item_id) itemName, (select photo from item where item.id=`order`.item_id) itemPhoto, per_price price, number from `user_order` inner join `order` on `order`.id=`user_order`.id inner join `user` on user_order.buyer_id = user.id where buyer_id = #{userId} limit #{pageNum},#{pageSize}")
    List<OrderVO> getOrderInfoByUserId(Long userId, Integer pageNum, Integer pageSize);

    @Select("select `order`.id orderId,(select nickname from user_details where user.id=`user_order`.buyer_id limit 1) buyerName, (select head from user_details where user_details.id=`user_order`.buyer_id) buyerHead, (select nickname from user_details where user.id=`order`.seller_id limit 1) sellerName, (select head from user_details where user_details.id=`order`.seller_id) sellerHead, (select `name` from item where item.id=`order`.item_id) itemName, (select photo from item where item.id=`order`.item_id) itemPhoto, per_price price, number from `user_order` inner join `order` on `order`.id=`user_order`.id inner join `user` on user_order.buyer_id = user.id where `order`.id = #{orderId}")
    OrderVO getOrderInfoByOrderId(Long orderId);

    @Select("select COUNT(*) from user_order where buyer_id=#{userId}")
    Integer getOrderNumByUserId(Long userId);

}
