package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.entity.Item;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: market
 * @author: HarryGao
 * @create: 2022-03-19 16:55
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Item> {

}
