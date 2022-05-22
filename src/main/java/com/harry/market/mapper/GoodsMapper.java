package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.controller.vo.ItemVO;
import com.harry.market.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Item> {

    @Select("select * from item where is_audit = 0")
    List<Item> audit();

//    @Select("select  id,name,introduction,photo,price from item limit #{pageNum},#{pageSize}")
    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,ud.head sellerHead,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where is_audit=2 order by rand() limit #{pageNum},#{pageSize}")
    List<ItemVO> selectDefault(Integer pageNum, Integer pageSize);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,ud.head sellerHead,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where is_audit=2 order by price ${order} limit #{pageNum},#{pageSize}")
    List<ItemVO> selectPage(String order, Integer pageNum, Integer pageSize);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,ud.head sellerHead,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where is_audit=2 order by gmt_modified desc limit #{pageNum},#{pageSize}")
    List<ItemVO> selectNewest(Integer pageNum, Integer pageSize);

    @Select("select i.is_audit,i.id,i.kind,name,ud.nickname sellerName,ud.head sellerHead,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where i.id = #{id}")
    List<ItemVO> findGoods(Long id);

    @Select ("select * from item where name like \"%\"#{nname}\"%\"")
    List<Item> findGoodsName(String nname);

    @Select ("Update item Set is_audit = 2 Where id = #{id}")
    List<Item> passAudit(Long id);

    @Select ("Update item Set is_audit = 1 Where id = #{id}")
    List<Item> unpassAudit(Long id);

    @Select("select `seller_id` from item where id = #{id}")
    List<BigInteger> getSellerId(BigInteger id);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where kind = #{kind} and is_audit=2 order by price ${order} limit #{pageNum},#{pageSize}")
    List<ItemVO> selectKind(String order, String kind, Integer pageNum, Integer pageSize);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where kind = #{kind} and is_audit=2 order by gmt_modified asc limit #{pageNum},#{pageSize}")
    List<ItemVO> selectKindNewest(String kind, Integer pageNum, Integer pageSize);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where kind = #{kind} and is_audit=2 order by price asc,number asc,gmt_modified desc limit #{pageNum},#{pageSize}")
    List<ItemVO> selectKindDefault(String kind, Integer pageNum, Integer pageSize);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where `name` like \"%${nname}%\" and is_audit=2")
    List<ItemVO> search(String nname);

    @Select("select COUNT(*) from item")
    Integer getTotalNum();

    @Select("select COUNT(*) from item where kind = #{kind} and is_audit=2")
    Integer getTotalNumByKind(String kind);

    @Select("select  i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where i.id=#{itemId}")
    ItemVO selectItembyId(Long itemId);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where is_audit=2 order by price ${order} limit #{pageNum},#{pageSize}")
    List<ItemVO> selectAll(String order,Integer pageNum, Integer pageSize);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where is_audit=2 order by gmt_modified asc limit #{pageNum},#{pageSize}")
    List<ItemVO> selectALlNewest(Integer pageNum, Integer pageSize);

    @Select("select  i.is_audit,i.id,i.kind,name,ud.nickname sellerName,i.introduction,photo,price,i.gmt_modified from item i inner join user_details ud on i.seller_id=ud.id where is_audit=2 order by price asc,number asc,gmt_modified desc limit #{pageNum},#{pageSize}")
    List<ItemVO> selectAllDefault(Integer pageNum, Integer pageSize);

}