package com.harry.market.controller;

import cn.hutool.Hutool;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.vo.ItemVO;
import com.harry.market.entity.User;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.UserMapper;
import com.harry.market.service.GoodService;
import com.harry.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/audit")
@CrossOrigin(origins ="*")
public class AuditController {

    @Autowired
    private GoodService goodService;

    @Autowired
    private UserService userService;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private UserMapper userMapper;

    @GetMapping(value = "/all",name = "0未审核/1不合格/2合格/3不限")
    public Result getAll(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<ItemVO> items = goodService.getAuditInfo(type, null,pageNum,pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,null,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/ESSJ",name = "0未审核/1不合格/2合格/3不限")
    public Result getESSJ(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "二手书籍";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType,pageNum,pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/FSXB",name = "0未审核/1不合格/2合格/3不限")
    public Result getFSXB(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "服饰鞋包";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/LSYP",name = "0未审核/1不合格/2合格/3不限")
    public Result getLSYP(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "零食饮品";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/MZHF",name = "0未审核/1不合格/2合格/3不限")
    public Result getMZHF(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "美妆护肤";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/RYJJ",name = "0未审核/1不合格/2合格/3不限")
    public Result getRYJJ(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "日用家居";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    //审核通过商品 0不通过 1通过
    @GetMapping("/audit/{id}")
    public Result passAudit(@PathVariable Long id,@RequestParam int pass) {
        ItemVO itemById = goodService.getItemById(id);
        if (itemById==null) {
            return Result.error(Constants.CODE_400,"商品id错误");
        }
        if (pass==0) {
            return Result.success(goodsMapper.unpassAudit(id));
        } else if (pass==1) {
            return Result.success(goodsMapper.passAudit(id));
        } else {
            return Result.error(Constants.CODE_400,"参数错误");
        }

    }

    @GetMapping("/user")
    public Result getAllUser(@RequestParam(defaultValue = "0") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "0",
                                     value = "0已发布升序|1已发布降序|2不合格升序|3不合格降序")Integer sort) {
        String s,o;
        if (sort==0) {
            s = "releasedNum";
            o = "asc";
        } else if (sort==1) {
            s = "releasedNum";
            o = "desc";
        } else if (sort==2) {
            s = "unpassedNum";
            o = "asc";
        } else if (sort==3) {
            s = "unpassedNum";
            o = "desc";
        } else {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        System.out.println(s+" "+o);
        return Result.success(userService.getUser(pageNum,pageSize,s,o),userService.getUserCount().toString());
    }

    @GetMapping("/delUser/{userId}")
    public Result deleteUser(@PathVariable Long userId) {
        if (userService.getUserById(userId)==null) {
            return Result.error(Constants.CODE_400,"该用户不存在或已被删除");
        }
        userService.delUser(userId);
        return Result.success();
    }


}
