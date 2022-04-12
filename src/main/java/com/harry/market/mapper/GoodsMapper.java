package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.controller.dto.ItemDTO;
import com.harry.market.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Item> {

    @Select("select * from item where is_audit = 0")
    List<Item> audit();

//    @Select("select  id,name,introduction,photo,price from item limit #{pageNum},#{pageSize}")
    @Select("select  i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id limit 1,5")
    List<ItemDTO> selectPage(Integer pageNum, Integer pageSize);

    @Select("select  * from item where id = #{id}")
    List<Item> findGoods(BigInteger id);

    @Select ("select * from item where name like \"%\"#{nname}\"%\"")
    List<Item> findGoodsName(String nname);

    @Select ("Update item Set is_audit = 1 Where id = #{id}")
    List<Item> passAudit(BigInteger id);

    @Select("select `seller_id` from item where id = #{id}")
    List<BigInteger> getSellerId(BigInteger id);
}