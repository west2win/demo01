package com.harry.market.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.reflect.ImmutableTypeToInstanceMap;
import com.harry.market.common.Result;
import com.harry.market.entity.Item;
import com.harry.market.entity.UserDetails;
import com.harry.market.mapper.GoodsMapper;
import com.harry.market.mapper.UserDetailsMapper;
import com.harry.market.service.UploadPicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @program: market
 * @authors: HarryGao 、 222100209_李炎东
 * @create: 2022-03-19 19:34
 */

@RestController
@RequestMapping("/photo")
public class PhotoController {

//    @Value("${files.upload.path}")
//    private String fileUploadPath;

    @Autowired
    UploadPicService uploadPicService;

    @Autowired
    UserDetailsMapper userDetailsMapper;

    @Autowired
    GoodsMapper goodsMapper;

//    /**
//     * @author HarryGao
//     * @param userId
//     * @param photo
//     * @return
//     * @throws IOException
//     */
//    @GetMapping("/{fileUUID}")
//    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
//        //根据文件的唯一标识码获取文件
//        File uploadFile = new File(fileUploadPath + fileUUID);
//        //设置输出流格式
//        ServletOutputStream os = response.getOutputStream();
//        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUUID,"UTF-8"));
//        response.setContentType("application/octet-stream");
//
//        //读取文件的字节流
//        os.write(FileUtil.readBytes(uploadFile));
//        os.flush();
//        os.close();
//    }

    /**
     * @author 222100209_李炎东
     * @param userId 用户Id
     * @param photo 用户头像
     * @return
     * @throws IOException
     */
    @PostMapping("/upHead")
    @ApiOperation("上传用户头像")
    public Result uploadUserHead(@RequestParam Long userId, @RequestParam MultipartFile photo) throws IOException {
        String folder = "item_photo/";
        String originalPhotoName = photo.getOriginalFilename();
        String photoType = FileUtil.extName(originalPhotoName);
        String photoUuid = IdUtil.fastSimpleUUID();
        String photoUUID = photoUuid + StrUtil.DOT + photoType;
        String photoURL = uploadPicService.uploadPic(photo,photoUUID,folder);
        UserDetails userDetails = new UserDetails();
        userDetails.setId(userId);
        userDetails.setHead(photoURL);
        userDetailsMapper.updateById(userDetails);
        return Result.success(photoURL);
    }

    /**
     * @author 222100209_李炎东
     * @param itemId 商品Id
     * @param photo 商品图片
     * @return
     * @throws IOException
     */
    @PostMapping("/upPhoto")
    @ApiOperation("上传商品照片")
    public Result uploadItemPhoto(@RequestParam Long itemId,@RequestParam MultipartFile photo) throws IOException {
        String folder = "user_head/";
        String originalPhotoName = photo.getOriginalFilename();
        String photoType = FileUtil.extName(originalPhotoName);
        String photoUuid = IdUtil.fastSimpleUUID();
        String photoUUID = photoUuid + StrUtil.DOT + photoType;
        String photoURL = uploadPicService.uploadPic(photo,photoUUID,folder);
        Item item = new Item();
        item.setId(itemId);
        item.setPhoto(photoURL);
        goodsMapper.updateById(item);
        return Result.success(photoURL);
    }

}