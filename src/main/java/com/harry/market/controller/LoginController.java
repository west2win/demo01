package com.harry.market.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * @program: demo01
 * @author: HarryGao
 * @create: 2022-05-18 22:44
 */

@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Resource
    private Producer producer;

    @GetMapping("/getCode")
    public void getKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //这里的text即为kaptcha生成的验证码中的文字，生成后放入session中，客户端请求登录时再取出进行比对
        String text = producer.createText();
        log.info("生成验证码:{}", text);
        request.getSession().setAttribute("code", text);
        BufferedImage image = producer.createImage(text);
        OutputStream os =  response.getOutputStream();
        ImageIO.write(image, "jpg", os);
        IOUtils.closeQuietly(os);

    }

    @GetMapping("/in")
    public String login(String code, HttpServletRequest request) {
        String text = request.getSession().getAttribute("code").toString();
        if(code == null || code == "") {
            return "验证码不能为空";
        }
        if(text.toLowerCase().equals(code.toLowerCase())) {
            return "验证码校验通过";
        }
        return "验证码校验不通过";
    }
}