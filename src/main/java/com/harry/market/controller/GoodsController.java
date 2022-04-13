package com.harry.market.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.entity.Item;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.service.UploadPicService;
import com.harry.market.service.UserService;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: market
 * @author: HarryGao
 * @create: 2022-03-19 16:40
 */

@ResponseBody
@RestController
@RequestMapping("/items")
@CrossOrigin(origins ="*")
public class GoodsController {

    @Autowired
    private UserService userService;

    @Resource
    private GoodsMapper goodsMapper;

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Autowired
    UploadPicService uploadPicService;
    String folder = "item_photo/";

    @PostMapping("/upload")
    public Result upload(@RequestParam String name, @RequestParam BigDecimal price, @RequestParam Integer number, @RequestParam MultipartFile intro, @RequestParam MultipartFile photo) throws IOException {

        String originalIntroName = intro.getOriginalFilename();
        String introType = FileUtil.extName(originalIntroName);

        String originalPhotoName = photo.getOriginalFilename();
        String photoType = FileUtil.extName(originalPhotoName);

        //先存储到磁盘
        File uploadParentFile = new File(fileUploadPath);

        //判断配置文件的目录是否存在，若不存在则创建一个新的文件目录
        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();
        }

        //定义一个文件唯一的标识码
        String introUuid = IdUtil.fastSimpleUUID();
        String introUUID = introUuid + StrUtil.DOT + introType;
        File uploadIntro = new File(fileUploadPath + introUUID);

        String photoUuid = IdUtil.fastSimpleUUID();
        String photoUUID = photoUuid + StrUtil.DOT + photoType;
        File uploadPhoto = new File(fileUploadPath + photoUUID);

        //把获取后的文件存储到磁盘目录
        intro.transferTo(uploadIntro);
        String introUrl = "http://484470xs49.qicp.vip/intro/" + introUUID;

//        photo.transferTo(uploadPhoto);
//        String photoURL = "http://484470xs49.qicp.vip/photo/" + photoUUID;

        // 改成上传AliOss对象存储了
        String photoURL = uploadPicService.uploadPic(photo,photoUUID,folder);

        Item saveGoods = new Item();
        Date date = new Date();

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 从SecurityContextHolder中获取当前用户id(也就是上传者/卖家的id)
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        BigInteger seller_id = userService.getUserId(username);

        saveGoods.setName(name);
        saveGoods.setIntroduction(introUrl);
        saveGoods.setPhoto(photoURL);
        saveGoods.setPrice(price);
        saveGoods.setNumber(number);
        saveGoods.setSeller_id(seller_id);
        saveGoods.set_audit(false);

        // mybatis-plus自动填充
//        saveGoods.set_deleted(false);
//        saveGoods.setGmt_create(Timestamp.valueOf(simpleDate.format(date)));
//        saveGoods.setGmt_modified(Timestamp.valueOf(simpleDate.format(date)));

        goodsMapper.insert(saveGoods);
        return Result.success();
    }


    //查看全部待审核商品
    @GetMapping("/findAudit")
    public Result findAudit() {
        if (goodsMapper.audit() != null) {
            return Result.success(goodsMapper.audit());
        }else{
            return Result.error(Constants.CODE_400, "没有未审核的商品");
        }
    }

    //审核通过商品
    @GetMapping("/audit/{id}")
    public Result passAudit(@PathVariable BigInteger id) {
        return Result.success(goodsMapper.passAudit(id));
        }

    //分页查询接口
    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        pageNum = (pageNum - 1) * pageSize;
        return Result.success(goodsMapper.selectPage(pageNum, pageSize));
    }

    //商品详情查询
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable BigInteger id) {
        return Result.success(goodsMapper.findGoods(id));
    }

    //模糊查询商品名称
    @GetMapping("/find/{nname}")
    public Result find(@PathVariable String nname) {
        if (goodsMapper.findGoodsName(nname).size() != 0) {
            return Result.success(goodsMapper.findGoodsName(nname));
        } else {
            return Result.error(Constants.CODE_400, "没有查询到类似商品");
        }
    }

    //分类查询商品
    @GetMapping("/kind/{kind}")
    public Result kind(@PathVariable String kind) {
        if (goodsMapper.selectKind(kind).size() != 0) {
            return Result.success(goodsMapper.selectKind(kind));
        } else {
            return Result.error(Constants.CODE_400, "没有查询到类似商品");
        }
    }

    //模糊查询商品
    @GetMapping("/search/{nname}")
    public Result search(@PathVariable String nname) {
        if (goodsMapper.search(nname).size() != 0) {
            return Result.success(goodsMapper.search(nname));
        } else {
            return Result.error(Constants.CODE_400, "没有查询到类似商品");
        }
    }


}
