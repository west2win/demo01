package com.harry.market.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.OrderDTO;
import com.harry.market.controller.vo.OrderVO;
import com.harry.market.entity.Item;
import com.harry.market.entity.Order;
import com.harry.market.entity.UserOrder;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.OrderMapper;
import com.harry.market.mapper.UserOrderMapper;
import com.harry.market.service.GoodService;
import com.harry.market.service.OrderService;
import com.harry.market.service.UserService;
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
            userOrder.setUser_id(buyer_id);
            userOrder.setItem_id(good_id);

            userOrderMapper.insert(userOrder);

            order.setId(userOrder.getId());
            order.setItem_id(good_id);
            order.setSeller_id(seller_id);
            order.setNumber(number);
            order.setPer_price(perPrice);
            order.setStatus(false);

            orderMapper.insert(order);

            return Result.success();
        }
    }

    @GetMapping("/Info/{id}")
    public Result getInfoById(@PathVariable Long id) {
        List<OrderVO> info = orderService.getInfoByUserId(id);
        if (!info.isEmpty()) {
            return Result.success(info,orderService.getOrderNumByUserId(id).toString());
        } else {
            return Result.error(Constants.CODE_400,"未查询到任何订单");
        }

    }


}
