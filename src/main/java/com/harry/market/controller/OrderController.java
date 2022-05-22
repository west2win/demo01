package com.harry.market.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.OrderDTO;
import com.harry.market.controller.vo.OrderVO;
import com.harry.market.entity.*;
import com.harry.market.mapper.*;
import com.harry.market.service.GoodService;
import com.harry.market.service.OrderService;
import com.harry.market.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@ResponseBody
@RestController
@RequestMapping("/order")
@CrossOrigin(origins ="*")
public class OrderController {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private UserOrderMapper userOrderMapper;

    @Resource
    private UserDetailsMapper userDetailsMapper;

    @Resource
    private BuyerMsgMapper buyerMsgMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private GoodService goodService;

    @Autowired
    private OrderService orderService;


//    //交易成功
//    @PostMapping("/deal")
//    public Result deal(@RequestParam BigInteger goodsId, @RequestParam Integer dealNumber){
//        if(goodsMapper.findGoods(goodsId).get((goodsId).intValue()).getNumber()<dealNumber){ //判断库存数量是否够卖
//            return Result.error(Constants.CODE_400, "库存商品不足");
//        }else{
//            return Result.success(orderMapper.deal(goodsId,dealNumber));
//        }
//    }

    //创建订单
    @PostMapping("/newOrder")
    @ApiOperation("新建订单")
    public Result newOrder(@RequestBody OrderDTO orderDTO) {
        Long buyer_id = orderDTO.getBuyer_id();
        Long good_id = orderDTO.getGood_id();
        Integer number = orderDTO.getNumber();
        if (buyer_id == null || good_id == null || number == null) {
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
            Long seller_id = item.getSeller_id();
            BigDecimal perPrice = item.getPrice();


//            // 从SecurityContextHolder中获取当前用户
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            String username;
//            if (principal instanceof UserDetails) {
//                username = ((UserDetails)principal).getUsername();
//            } else {
//                username = principal.toString();
//            }
            userOrder.setId(IdWorker.getId(userOrder));
            userOrder.setBuyer_id(buyer_id);
            userOrder.setItem_id(good_id);

            userOrderMapper.insert(userOrder);

            order.setId(userOrder.getId());
            order.setItem_id(good_id);
            order.setSeller_id(seller_id);
            order.setNumber(number);
            order.setPer_price(perPrice);
            order.setStatus(false);

            orderMapper.insert(order);

            BuyerMsg buyerMsg = new BuyerMsg();
            buyerMsg.setId(buyer_id);
            buyerMsg.setOrder_id(userOrder.getId());

            buyerMsgMapper.updateById(buyerMsg);

            OrderVO orderVO = orderService.getOrdermsgByOrderId(userOrder.getId());
            System.out.println(orderVO);

            return Result.success(orderVO);
        }
    }

//    @GetMapping("/itemInfo/{userId}")
//    @ApiOperation("[我买的]获取某用户所有订单信息")
    public Result getInfoById(@PathVariable Long userId,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<OrderVO> info = orderService.getInfoByUserId(userId,pageNum,pageSize);
        if (!info.isEmpty()) {
            return Result.success(info,orderService.getOrderNumByUserId(userId).toString());
        } else {
            return Result.error(Constants.CODE_400,"未查询到任何订单");
        }
    }

    @GetMapping("/itemInfo/{userId}")
    @ApiOperation("[我买的]获取某用户待收货/已完成订单信息(0为待收货/1为已完成/2为全部)")
    public Result getInfoBySort(@PathVariable Long userId,@RequestParam Integer sort,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<OrderVO> info;
        if (sort==0||sort==1) {
            info = orderService.getInfoBySort(userId,sort,pageNum,pageSize);
            if (info.isEmpty()) {
                return Result.error(Constants.CODE_400,"未查询到任何订单");
            }
            return Result.success(info,orderService.getCountBySort(userId,sort).toString());
        } else if (sort==2) {
            info = orderService.getInfoByUserId(userId,pageNum,pageSize);
            if (info.isEmpty()) {
                return Result.error(Constants.CODE_400,"未查询到任何订单");
            }
            return Result.success(info,orderService.getOrderNumByUserId(userId).toString());
        } else {
            return Result.error(Constants.CODE_400,"参数错误");
        }
    }

    @GetMapping("/buyerInfo/{userId}")
    @ApiOperation("获取某订单买家地址信息等")
    public Result getBuyerInfo(@PathVariable Long userId) {
        BuyerMsg buyerMsg = orderService.getBuyerMsg(userId);
        return Result.success(buyerMsg);
    }

    @PostMapping("/chgBuyerInfo")
    @ApiOperation("修改卖家信息(地址电话等)")
    public Result changeBuyerInfo(@RequestBody BuyerMsg buyerMsg) {
        orderService.modifyBuyerMsg(buyerMsg);
        return Result.success(buyerMsg);
    }

    @DeleteMapping("/cancel/{orderId}")
    @ApiOperation("取消顶单")
    public Result cancelOrder(@PathVariable Long orderId) {
        orderMapper.deleteById(orderId);
        userOrderMapper.deleteById(orderId);
        return Result.success();
    }

    @GetMapping("/confirm/{orderId}")
    @ApiOperation("确认收货")
    public Result confirm(@PathVariable Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(true);
        orderMapper.updateById(order);
        return Result.success();
    }


}
