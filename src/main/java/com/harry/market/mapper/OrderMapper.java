package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.controller.dto.OrderVO;
import com.harry.market.entity.Item;
import com.harry.market.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("Update item Set number = number - #{dealNumber} Where id = #{goodsId}")
    List<Item> deal(Long goodsId, Integer dealNumber);

    @Select("select seller_id from item where id = #{goodId}")
    String getSellerId(Long goodId);

    @Select("select (select username from user where user.id=`user_order`.user_id) buyerName, (select head from user_details where user_details.id=`user_order`.user_id) buyerHead, (select username from user where user.id=`order`.seller_id) sellerName, (select head from user_details where user_details.id=`order`.seller_id) sellerHead, (select `name` from item where item.id=`order`.item_id) itemName, (select photo from item where item.id=`order`.item_id) itemPhoto, per_price price, number from `user_order` inner join `order` on `order`.id=`user_order`.id inner join `user` on user_order.user_id = user.id where user_id = #{userId}")
    List<OrderVO> getOrderInfoByUserId(Long userId);

    @Select("select COUNT(*) from user_order where user_id=#{userId}")
    Integer getOrderNumByUserId(Long userId);

}
