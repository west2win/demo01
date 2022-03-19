package com.harry.market.service;

import com.aliyun.oss.OSSClient;
import com.harry.market.config.AliOssConfig;
import com.harry.market.utils.OssUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class UploadPicService {

    @Autowired
    private OSSClient ossClient;

    /**
     *
     * @param multipartFile
     * @param fileName
     * @param folder
     * @return 图片url
     * @throws FileNotFoundException
     */
    public String uploadPic(MultipartFile multipartFile,String fileName, String folder) throws FileNotFoundException {
        InputStream  inputstream  = new  FileInputStream(fileName);
        return OssUtills.saveImg(multipartFile,ossClient,folder);
    }
}
