package com.harry.market.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.ItemDTO;
import com.harry.market.controller.vo.ItemVO;
import com.harry.market.entity.Item;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.service.GoodService;
import com.harry.market.service.UploadPicService;
import com.harry.market.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 222100209_李炎东
 */

@ResponseBody
@RestController
@RequestMapping("/items")
@CrossOrigin(origins ="*")
public class GoodsController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodService goodService;
    @Resource
    private GoodsMapper goodsMapper;

//    @Value("${files.upload.path}")
//    private String fileUploadPath;

    //商品图片存到OssBucket里面对应的目录下
    @Autowired
    UploadPicService uploadPicService;
    String folder = "item_photo/";

//    /**
//     * @author HarryGao
//     */
//    @PostMapping("/upload")
//    public Result upload(@RequestParam Long id,@RequestParam String name, @RequestParam BigDecimal price, @RequestParam Integer number, @RequestParam MultipartFile intro, @RequestParam MultipartFile photo) throws IOException {
//
//        String originalIntroName = intro.getOriginalFilename();
//        String introType = FileUtil.extName(originalIntroName);
//
//        String originalPhotoName = photo.getOriginalFilename();
//        String photoType = FileUtil.extName(originalPhotoName);
//
//        //先存储到磁盘
//        File uploadParentFile = new File(fileUploadPath);
//
//        //判断配置文件的目录是否存在，若不存在则创建一个新的文件目录
//        if (!uploadParentFile.exists()) {
//            uploadParentFile.mkdirs();
//        }
//
//        //定义一个文件唯一的标识码
//        String introUuid = IdUtil.fastSimpleUUID();
//        String introUUID = introUuid + StrUtil.DOT + introType;
//        File uploadIntro = new File(fileUploadPath + introUUID);
//
//        String photoUuid = IdUtil.fastSimpleUUID();
//        String photoUUID = photoUuid + StrUtil.DOT + photoType;
//        File uploadPhoto = new File(fileUploadPath + photoUUID);
//
//        //把获取后的文件存储到磁盘目录
//        intro.transferTo(uploadIntro);
//        String introUrl = "http://484470xs49.qicp.vip/intro/" + introUUID;
//
////        photo.transferTo(uploadPhoto);
////        String photoURL = "http://484470xs49.qicp.vip/photo/" + photoUUID;
//
//        // 改成上传AliOss对象存储了
//        String photoURL = uploadPicService.uploadPic(photo,photoUUID,folder);
//
//        Item saveGoods = new Item();
//        Date date = new Date();
//
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////        // 从SecurityContextHolder中获取当前用户id(也就是上传者/卖家的id)
////        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        String username;
////        if (principal instanceof UserDetails) {
////            username = ((UserDetails)principal).getUsername();
////        } else {
////            username = principal.toString();
////        }
////        Long seller_id = userService.getUserId(username);
//        Long seller_id = id;
//
//        saveGoods.setName(name);
//        saveGoods.setIntroduction(introUrl);
//        saveGoods.setPhoto(photoURL);
//        saveGoods.setPrice(price);
//        saveGoods.setNumber(number);
//        saveGoods.setSeller_id(seller_id);
//        saveGoods.set_audit(false);
//
//        // mybatis-plus自动填充
////        saveGoods.set_deleted(false);
////        saveGoods.setGmt_create(Timestamp.valueOf(simpleDate.format(date)));
////        saveGoods.setGmt_modified(Timestamp.valueOf(simpleDate.format(date)));
//
//        goodsMapper.insert(saveGoods);
//        return Result.success();
//    }


//    //查看全部待审核商品
//    @GetMapping("/findAudit")
//    public Result findAudit() {
//        if (goodsMapper.audit() != null) {
//            return Result.success(goodsMapper.audit());
//        }else{
//            return Result.error(Constants.CODE_400, "没有未审核的商品");
//        }
//    }

    /**
     * @author 222100209_李炎东
     * @param order 排序参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    //分页查询接口
    @ApiOperation("全种类分页")
//    @ApiImplicitParam(name = "order",value = "默认综合|asc价格升序|desc价格降序|newest最新")
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "default") String order,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Result result;
        if ("asc".equals(order)||"desc".equals(order)) {
            List<ItemVO> items = goodsMapper.selectPage(order, pageNum*pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNum().toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("newest".equals(order)) {
            List<ItemVO> items = goodsMapper.selectNewest(pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNum().toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("default".equals(order)) {
            List<ItemVO> items = goodsMapper.selectDefault(pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNum().toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else {
            result = Result.error(Constants.CODE_400,"传参错了");
        }

        return result;
    }

    /**
     * @author 222100209_李炎东
     * @param id 商品Id
     * @return
     */
    //商品详情查询
    @GetMapping("/detail/{id}")
    @ApiOperation("查看商品详情")
    public Result detail(@PathVariable Long id) {
        return Result.success(goodService.findGoods(id));
    }

    //模糊查询商品名称
//    @GetMapping("/find/{nname}")
//    public Result find(@PathVariable String nname) {
//        if (goodsMapper.findGoodsName(nname).size() != 0) {
//            return Result.success(goodsMapper.findGoodsName(nname));
//        } else {
//            return Result.error(Constants.CODE_400, "没有查询到类似商品");
//        }
//    }

//    //分类查询商品
//    @GetMapping("/kind/{kind}")
//    public Result kind(@PathVariable String kind) {
//        if (goodsMapper.selectKind(kind).size() != 0) {
//            return Result.success(goodsMapper.selectKind(kind));
//        } else {
//            return Result.error(Constants.CODE_400, "没有查询到类似商品");
//        }
//    }

    /**
     * @author 222100209_李炎东
     * @param order 排序
     * @param pageNum 第?页
     * @param pageSize 一页有?条数据
     * @return
     */
    @ApiOperation("服饰鞋包")
//    @ApiImplicitParam(name = "order",value = "默认综合|asc价格升序|desc价格降序|newest最新")
    @GetMapping("/find/fsxb")
    public Result findFSXB(@RequestParam(defaultValue = "default") String order,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Result result;
        if ("asc".equals(order)||"desc".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKind(order, "服饰鞋包", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("服饰鞋包").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("newest".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindNewest("服饰鞋包", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("服饰鞋包").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("default".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindDefault("服饰鞋包", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("服饰鞋包").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else {
            result = Result.error(Constants.CODE_400,"传参错了");
        }

        return result;
    }

    /**
     * @author 222100209_李炎东
     * @param order 排序
     * @param pageNum 第?页
     * @param pageSize 一页有?条数据
     * @return
     */
    @ApiOperation("美妆护肤")
//    @ApiImplicitParam(name = "order",value = "默认综合|asc价格升序|desc价格降序|newest最新")
    @GetMapping("/find/mzhf")
    public Result findMZHF(@RequestParam(defaultValue = "asc") String order,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Result result;
        if ("asc".equals(order)||"desc".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKind(order, "美妆护肤", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("美妆护肤").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("newest".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindNewest("美妆护肤", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("美妆护肤").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("default".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindDefault("美妆护肤", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("美妆护肤").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else {
            result = Result.error(Constants.CODE_400,"传参错了");
        }

        return result;
    }

    /**
     * @author 222100209_李炎东
     * @param order 排序
     * @param pageNum 第?页
     * @param pageSize 一页有?条数据
     * @return
     */
    @ApiOperation("二手书籍")
//    @ApiImplicitParam(name = "order",value = "默认综合|asc价格升序|desc价格降序|newest最新")
    @GetMapping("/find/essj")
    public Result findESSj(@RequestParam(defaultValue = "asc") String order,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Result result;
        if ("asc".equals(order)||"desc".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKind(order, "二手书籍", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("二手书籍").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("newest".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindNewest("二手书籍", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("二手书籍").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("default".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindDefault("二手书籍", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("二手书籍").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else {
            result = Result.error(Constants.CODE_400,"传参错了");
        }

        return result;
    }

    /**
     * @author 222100209_李炎东
     * @param order 排序
     * @param pageNum 第?页
     * @param pageSize 一页有?条数据
     * @return
     */
    @ApiOperation("日用家居")
//    @ApiImplicitParam(name = "order",value = "默认综合|asc价格升序|desc价格降序|newest最新")
    @GetMapping("/find/ryjj")
    public Result findRYJJ(@RequestParam(defaultValue = "asc") String order,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Result result;
        if ("asc".equals(order)||"desc".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKind(order, "日用家居", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("日用家居").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("newest".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindNewest("日用家居", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("日用家居").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("default".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindDefault("日用家居", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("日用家居").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else {
            result = Result.error(Constants.CODE_400,"传参错了");
        }

        return result;
    }

    /**
     * @author 222100209_李炎东
     * @param order 排序
     * @param pageNum 第?页
     * @param pageSize 一页有?条数据
     * @return
     */
    @ApiOperation("零食饮品")
//    @ApiImplicitParam(name = "order",value = "默认综合|asc价格升序|desc价格降序|newest最新")
    @GetMapping("/find/lsyp")
    public Result findLSYP(@RequestParam(defaultValue = "asc") String order,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Result result;
        if ("asc".equals(order)||"desc".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKind(order, "零食饮品", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("零食饮品").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("newest".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindNewest("零食饮品", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("零食饮品").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("default".equals(order)) {
            List<ItemVO> items = goodsMapper.selectKindDefault("零食饮品", pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNumByKind("零食饮品").toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else {
            result = Result.error(Constants.CODE_400,"传参错了");
        }

        return result;
    }

    /**
     * @author 222100209_李炎东
     * @param order 排序
     * @param pageNum 第?页
     * @param pageSize 一页有?条数据
     * @return
     */
    @ApiOperation("不分类 获取全部 带排序 分页")
    @GetMapping("/find/all")
    public Result findAll(@RequestParam(defaultValue = "asc") String order,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Result result;
        if ("asc".equals(order)||"desc".equals(order)) {
            List<ItemVO> items = goodsMapper.selectAll(order, pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNum().toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("newest".equals(order)) {
            List<ItemVO> items = goodsMapper.selectALlNewest(pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNum().toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else if ("default".equals(order)) {
            List<ItemVO> items = goodsMapper.selectAllDefault(pageNum * pageSize, pageSize);
            if (!items.isEmpty()) {
                result = Result.success(items,goodsMapper.getTotalNum().toString());
            } else {
                result = Result.error(Constants.CODE_400, "没有查询到类似商品");
            }
        } else {
            result = Result.error(Constants.CODE_400,"传参错了");
        }

        return result;
    }


    /**
     * @author 222100209_李炎东
     * @param nname 模糊查询
     * @param sort 排序参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
        //模糊查询商品
    @GetMapping("/search/{nname}")
    @ApiOperation("模糊查询(0综合|1价格升序|2价格降序|3最新)")
    public Result search(@PathVariable String nname,@RequestParam Integer sort,@RequestParam(defaultValue = "0") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize) {
        if (sort < 0 || sort>3) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
//        if (goodsMapper.search(nname).size() != 0) {
        List<ItemVO> search = goodService.search(nname, sort, pageNum, pageSize);
        if (!search.isEmpty()) {
            return Result.success(search,goodService.getSearchCount(nname,sort).toString());
        } else {
            return Result.error(Constants.CODE_400, "没有查询到类似商品");
        }
    }

    /**
     * @author 222100209_李炎东
     * @param photo 商品图片
     * @param kind 商品类型
     * @param name 商品名
     * @param intro 商品介绍
     * @param price 商品价格
     * @param number 商品数量
     * @param seller_id 卖家Id
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ApiOperation("上传商品")
    public Result uploadNewItem(@RequestParam(required = false) MultipartFile photo,
                                @RequestParam String kind,
                                @RequestParam String name,
                                @RequestParam(required = false) String intro,
                                @RequestParam BigDecimal price,
                                @RequestParam Integer number,
                                @RequestParam Long seller_id)
            throws IOException {
        ItemDTO itemDTO = new ItemDTO();
        String folder = "user_head/";
        if (photo!=null) {
            String originalPhotoName = photo.getOriginalFilename();
            String photoType = FileUtil.extName(originalPhotoName);
            String photoUuid = IdUtil.fastSimpleUUID();
            String photoUUID = photoUuid + StrUtil.DOT + photoType;
            String photoURL = uploadPicService.uploadPic(photo,photoUUID,folder);
            itemDTO.setPhoto(photoURL);
        } else {
            itemDTO.setPhoto("https://market-information.oss-cn-heyuan.aliyuncs.com/item_photo/default.png");
        }
        itemDTO.setKind(kind);
        itemDTO.setName(name);
        itemDTO.setIntro(intro);
        itemDTO.setPrice(price);
        itemDTO.setNumber(number);
        itemDTO.setSeller_id(seller_id);
        Long itemId = goodService.uploadItem(itemDTO);
        ItemVO itemVo = goodService.getItemById(itemId);
        System.out.println(itemVo);
        return Result.success(itemVo);
    }

    /**
     * @author 222100209_李炎东
     * @param itemDTO 商品新的信息
     * @return
     */
    @PostMapping("/chgItemInfo")
    @ApiOperation("修改商品信息")
    public Result chgItemInfo(@RequestBody ItemDTO itemDTO) {
        goodService.chgItemInfo(itemDTO);
        return Result.success(goodService.getGood(itemDTO.getItemId()));
    }

    /**
     * @author 222100209_李炎东
     * @param userId 用户Id
     * @param sort 筛选参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    @GetMapping("/itemInfo/{userId}")
    @ApiOperation("[我卖的]0未售出|1已售出|2全部")
    public Result getInfoById(@PathVariable Long userId,@RequestParam Integer sort,@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        if (sort==0||sort==1||sort==2) {
            List<ItemVO> info;
            info = goodService.getInfoBySort(userId,sort,pageNum,pageSize);
            if (info.isEmpty()) {
                return Result.error(Constants.CODE_400,"未查询到任何商品");
            }
            return Result.success(info,goodService.getCountBySort(userId,sort).toString());
        } else {
            return Result.error(Constants.CODE_400,"参数错误");
        }
    }

    /**
     * @author 222100209_李炎东
     * @param itemId 商品Id
     * @return
     */
    @DeleteMapping("/del/{itemId}")
    @ApiOperation("下架商品")
    public Result delItem(@PathVariable Long itemId) {
        goodsMapper.deleteById(itemId);
        return Result.success();
    }
}
