package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @program: market
 * @author: HarryGao
 * @create: 2022-03-19 16:55
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Item> {

    @Select("select * from item where is_audit = 0")
    List<Item> audit();

    @Select("select  id,name,introduction,photo,price from item limit #{pageNum},#{pageSize}")
    List<Item> selectPage(Integer pageNum, Integer pageSize);

    @Select("select  * from item where id = #{id}")
    List<Item> findGoods(BigInteger id);

    @Select ("select * from item where name like \"%\"#{nname}\"%\"")
    List<Item> findGoodsName(String nname);

    @Select ("Update item Set is_audit = 1 Where id = #{id}")
    List<Item> passAudit(BigInteger id);

}