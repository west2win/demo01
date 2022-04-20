package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    List<Item> deal(BigInteger goodsId, Integer dealNumber);

    @Select("select seller_id from item where id = #{goodId}")
    String getSellerId(BigInteger goodId);

    @Insert("")
    void newUserOder();

    @Insert("")
    void newOrder();

}
