package com.harry.market.controller;

import cn.hutool.core.util.StrUtil;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.OrderDTO;
import com.harry.market.entity.Item;
import com.harry.market.entity.Order;
import com.harry.market.entity.UserOrder;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.OrderMapper;
import com.harry.market.mapper.UserOrderMapper;
import com.harry.market.service.GoodService;
import com.harry.market.service.UserService;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

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

    @Resource
    private UserOrderMapper userOrderMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private GoodService goodService;

    //交易成功
    @PostMapping("/deal")
    public Result deal(@RequestParam BigInteger goodsId, @RequestParam Integer dealNumber){
        if(goodsMapper.findGoods(goodsId).get((goodsId).intValue()).getNumber()<dealNumber){ //判断库存数量是否够卖
            return Result.error(Constants.CODE_400, "库存商品不足");
        }else{
            return Result.success(orderMapper.deal(goodsId,dealNumber));
        }
    }

    //创建订单
    @PostMapping("/newOrder")
    public Result newOrder(@RequestBody OrderDTO orderDTO) {
        BigInteger good_id = orderDTO.getGood_id();
        Integer number = orderDTO.getNumber();
        if (good_id == null || number == null) {
            return Result.error(Constants.CODE_400, "参数错误");
        } else if (number.intValue() <= 0) {
            return Result.error(Constants.CODE_400, "购买数量不正确");
        } else {
            Order order = new Order();
            UserOrder userOrder = new UserOrder();

            Item item = goodService.getGood(good_id);
            if (item == null) {
                return Result.error(Constants.CODE_400,"不存在该商品或该商品未经审核");
            }
            String goodName = item.getName();
            BigInteger seller_id = item.getSeller_id();
            BigDecimal perPrice = item.getPrice();



            // 从SecurityContextHolder中获取当前用户
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            BigInteger userId = userService.getUserId(username);

            userOrder.setUser_id(userId);
            userOrder.setItem_id(good_id);

            userOrderMapper.insert(userOrder);


            order.setItem(goodName);
            order.setSeller_id(seller_id);
            order.setNumber(number);
            order.setPer_price(perPrice);
            order.setStatus(false);

            orderMapper.insert(order);

            return Result.success();
        }
    }

}
