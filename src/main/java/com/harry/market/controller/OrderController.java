package com.harry.market.controller;

import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.OrderMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * @program: demo02
 * @author: HarryGao
 * @create: 2022-03-20 18:30
 */
public class OrderController {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private GoodsMapper goodsMapper;

    //交易成功
    @PostMapping("/deal")
    public Result deal(@RequestParam BigInteger goodsId, @RequestParam Integer dealNumber){
        if(goodsMapper.findGoods(goodsId).get((goodsId).intValue()).getNumber()<dealNumber){ //判断库存数量是否够卖
            return Result.error(Constants.CODE_400, "库存商品不足");
        }else{
            return Result.success(orderMapper.deal(goodsId,dealNumber));
        }
    }
}
