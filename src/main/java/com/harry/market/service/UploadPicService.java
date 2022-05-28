package com.harry.market.service;

import com.aliyun.oss.OSSClient;
import com.harry.market.config.AliOssConfig;
import com.harry.market.utils.OssUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 222100209_李炎东
 * @apiNote 上传图片
 */
@Service
public class UploadPicService {

    @Autowired
    private OSSClient ossClient;

    /**
     * @author 222100209_李炎东
     * @param multipartFile 图片
     * @param fileName 图片名
     * @param folder Oss中存储路径
     * @return 图片url
     * @throws FileNotFoundException
     */
    public String uploadPic(MultipartFile multipartFile,String fileName, String folder) throws IOException {
//        InputStream in = multipartFile.getInputStream();
        return OssUtills.saveImg(multipartFile,ossClient,folder);
    }
}
