package com.harry.market.controller;

import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.vo.ItemVO;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.service.GoodService;
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
    GoodService goodService;

    @Resource
    GoodsMapper goodsMapper;

    @GetMapping(value = "/all/{type}",name = "0未审核/1不合格/2合格/3不限")
    public Result getAll(@PathVariable Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<ItemVO> items = goodService.getAuditInfo(type, null,pageNum,pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,null,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/ESSJ/{type}",name = "0未审核/1不合格/2合格/3不限")
    public Result getESSJ(@PathVariable Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "二手书籍";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType,pageNum,pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/FSXB/{type}",name = "0未审核/1不合格/2合格/3不限")
    public Result getFSXB(@PathVariable Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "服饰鞋包";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/LSYP/{type}",name = "0未审核/1不合格/2合格/3不限")
    public Result getLSYP(@PathVariable Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "零食饮品";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/MZHF/{type}",name = "0未审核/1不合格/2合格/3不限")
    public Result getMZHF(@PathVariable Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "美妆护肤";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    @GetMapping(value = "/RYJJ/{type}",name = "0未审核/1不合格/2合格/3不限")
    public Result getRYJJ(@PathVariable Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "日用家居";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    //审核通过商品
    @GetMapping("/audit/{id}")
    public Result passAudit(@PathVariable Long id) {
        return Result.success(goodsMapper.passAudit(id));
    }



}
