package com.harry.market.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.auth.ECSMetadataServiceCredentialsFetcher;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.entity.User;
import com.harry.market.entity.UserDetails;
import com.harry.market.service.MailService;
import com.harry.market.service.UserService;
import com.harry.market.utils.RedisUtils;
import com.harry.market.utils.VerCodeGenerateUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author 222100209_李炎东
 * @apiNote 通过JavaMailSender发送高级邮件，作为邮箱验证码并存入redis
 */

@ResponseBody
@RestController
@RequestMapping("/mail")
@CrossOrigin(origins ="*")
public class MailController {
    @Autowired
    private JavaMailSenderImpl mailSender;
//    @Autowired
//    private RedisUtils redisTemplateUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @author 222100209_李炎东
     * @param email 用户邮箱
     * @return
     */
    @GetMapping(value = "/send/{email}")
    @ApiOperation("给对应用户发送邮件验证码")
    public Result sendEmail(@PathVariable String email) {
//        List<UserDetails> user = userService.getUserbyEmail(email);
//        if (user.isEmpty()) {
//            return Result.error(Constants.CODE_401,"不存在该邮箱注册的用户");
//        }

//        System.out.println(email);
//        email = "362664609@qq.com";
        //key 邮箱号  value 验证码
        String code = redisTemplate.opsForValue().get(email);
        //从redis获取验证码，如果获取获取到，返回ok
        if (!StringUtils.isEmpty(code)) {
            System.out.println("redis中已存在验证码");
            return Result.success(code);
        }
        //如果从redis获取不到，生成新的4位验证码
        code = VerCodeGenerateUtil.getVerCode();
        //调用service方法，通过邮箱服务进行发送
        boolean isSend = mailService.sendMail(email, code);
        //生成验证码放到redis里面，设置有效时间
        if (isSend) {
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return Result.success();
        } else {
            return Result.error(Constants.CODE_600,"发送邮箱验证码失败");
        }
    }

    /**
     * @author 222100209_李炎东
     * @param email 用户邮箱
     * @param VerCode 收到的验证码
     * @return
     */
    @GetMapping("/cmp")
    @ApiOperation("验证邮箱验证码")
    public Result compareCode(@RequestParam String email,@RequestParam String VerCode) {
        String code = redisUtils.get(email);
        if (code.equals(VerCode)) {
            return Result.success();
        } else {
            return Result.error(Constants.CODE_401,"验证码错误");
        }
    }

}
