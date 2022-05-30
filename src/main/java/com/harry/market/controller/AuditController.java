package com.harry.market.controller;

import cn.hutool.Hutool;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.vo.AuditUserVO;
import com.harry.market.controller.vo.ItemVO;
import com.harry.market.entity.User;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.UserMapper;
import com.harry.market.service.GoodService;
import com.harry.market.service.MessageService;
import com.harry.market.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 222100209_李炎东
 */

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
    @Autowired
    private MessageService messageService;

    /**
     * @author 222100209_李炎东
     * @param type 筛选项
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return ItemVO数据
     */
    @GetMapping(value = "/all",name = "0未审核/1不合格/2合格/3不限")
    @ApiOperation("获取所有商品信息")
    public Result getAll(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<ItemVO> items = goodService.getAuditInfo(type, null,pageNum,pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,null,pageNum,pageSize).toString());
    }

    /**
     * @author 222100209_李炎东
     * @param type 筛选项
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return ItemVO数据
     */
    @GetMapping(value = "/ESSJ",name = "0未审核/1不合格/2合格/3不限")
    @ApiOperation("获取二手书籍相关商品信息")
    public Result getESSJ(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "二手书籍";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType,pageNum,pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    /**
     * @author 222100209_李炎东
     * @param type 筛选项
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return ItemVO数据
     */
    @GetMapping(value = "/FSXB",name = "0未审核/1不合格/2合格/3不限")
    @ApiOperation("获取服饰鞋包相关商品信息")
    public Result getFSXB(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "服饰鞋包";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    /**
     * @author 222100209_李炎东
     * @param type 筛选项
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return ItemVO数据
     */
    @GetMapping(value = "/LSYP",name = "0未审核/1不合格/2合格/3不限")
    @ApiOperation("获取零食饮品相关商品信息")
    public Result getLSYP(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "零食饮品";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    /**
     * @author 222100209_李炎东
     * @param type 筛选项
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return ItemVO数据
     */
    @GetMapping(value = "/MZHF",name = "0未审核/1不合格/2合格/3不限")
    @ApiOperation("获取美妆护肤相关商品信息")
    public Result getMZHF(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "美妆护肤";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    /**
     * @author 222100209_李炎东
     * @param type 筛选项
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return ItemVO数据
     */
    @GetMapping(value = "/RYJJ",name = "0未审核/1不合格/2合格/3不限")
    @ApiOperation("获取日用家居相关商品信息")
    public Result getRYJJ(@RequestParam Short type,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        String itemType = "日用家居";
        List<ItemVO> items = goodService.getAuditInfo(type, itemType, pageNum, pageSize);
        if (items.isEmpty()) {
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
        return Result.success(items,goodService.getAuditCount(type,itemType,pageNum,pageSize).toString());
    }

    /**
     * @author 222100209_李炎东
     * @param id 商品id
     * @param pass 是否通过
     * @return
     */
    //审核通过商品 0不通过 1通过
    @ApiOperation("审核通过商品(0不通过 1通过)")
    @GetMapping("/audit/{id}")
    public Result passAudit(@PathVariable Long id,@RequestParam int pass) {
        ItemVO itemById = goodService.getItemById(id);
        if (itemById==null) {
            return Result.error(Constants.CODE_400,"商品id错误");
        }
        Long sellerId = Long.parseLong(itemById.getSellerId());
        if (pass==0) {
            messageService.sendSys("您的商品 "+itemById.getName()+" 未通过审核，请尝试排查错误后重新提交",sellerId);
            return Result.success(goodsMapper.unpassAudit(id));
        } else if (pass==1) {
            messageService.sendSys("恭喜您！您的商品 "+itemById.getName()+" 已通过审核！",sellerId);
            return Result.success(goodsMapper.passAudit(id));
        } else {
            return Result.error(Constants.CODE_400,"参数错误");
        }

    }

    /**
     * @author 222100209_李炎东
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @param sort 筛选项
     * @return
     */
    @GetMapping("/user")
    @ApiOperation("获取所用用户信息(0已发布升序|1已发布降序|2不合格升序|3不合格降序)")
    public Result getAllUser(@RequestParam(defaultValue = "0") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "0") Integer sort) {
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
//        System.out.println(s+" "+o);
        return Result.success(userService.getUser(pageNum,pageSize,s,o),userService.getUserCount().toString());
    }

    /**
     * @author 222100209_李炎东
     * @param userId 用户Id
     * @return
     */
    @GetMapping("/delUser/{userId}")
    @ApiOperation("封禁用户")
    public Result deleteUser(@PathVariable Long userId) {
        if (userService.getUserById(userId)==null) {
            return Result.error(Constants.CODE_400,"该用户不存在或已被删除");
        }
        messageService.sendSys("很抱歉,您的用户因某种原因已被封禁",userId);
        userService.delUser(userId);
        return Result.success();
    }

    /**
     * @author 222100209_李炎东
     * @param nname 模糊查询
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    @GetMapping("/search/{nname}")
    @ApiOperation("模糊查询商品信息")
    public Result searchItem(@PathVariable String nname,
                         @RequestParam(defaultValue = "0") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize) {
        List<ItemVO> itemVOS = goodService.searchForAudit(nname, pageNum, pageSize);
        if (itemVOS.isEmpty()) {
            return Result.error(Constants.CODE_400,"未查询到任何商品");
        } else {
            return Result.success(itemVOS,goodService.getSearchForAuditCount(nname).toString());
        }

    }

    /**
     * @author 222100209_李炎东
     * @param nname 模糊查询
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    @GetMapping("/searchUser/{nname}")
    @ApiOperation("模糊查询用户信息")
    public Result searchUser(@PathVariable String nname,
                         @RequestParam(defaultValue = "0") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize) {
        List<AuditUserVO> users = userService.searchUser(nname, pageNum, pageSize);
        if (users.isEmpty()) {
            return Result.error(Constants.CODE_400,"未查询到用户");
        } else {
            return Result.success(users,userService.searchUserCount(nname).toString());
        }
    }


}
