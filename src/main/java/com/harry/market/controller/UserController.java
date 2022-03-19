package com.harry.market.controller;

import cn.hutool.core.util.StrUtil;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.UserDTO;
import com.harry.market.entity.User;
import com.harry.market.mapper.UserMapper;
import com.harry.market.service.UserService;
import com.harry.market.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======
import javax.annotation.Resource;
>>>>>>> a83b967 (商品上传)
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


<<<<<<< HEAD
import javax.annotation.Resource;


=======
>>>>>>> a83b967 (商品上传)

@ResponseBody
@RestController
@RequestMapping("/user")
@CrossOrigin(origins ="*")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }else if(userMapper.findAdmin(userDTO.getId())!=null){
            return Result.adminSuccess();
        }else{
            userService.login(userDTO);
            return Result.success(userDTO.getId());
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = Md5Utils.code(userDTO.getPassword());
        userDTO.setPassword(password);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }else if(userMapper.sameName(username).size()!=0){
            return Result.error(Constants.CODE_400, "此用户名已被注册");
        }else {
            User saveUser = new User();
            Date date = new Date();

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            saveUser.setUsername(username);
            saveUser.setPassword(password);
            saveUser.setPerm("用户");
            saveUser.setIs_deleted(0);
            saveUser.setGmt_create(Timestamp.valueOf(simpleDate.format(date)));
            saveUser.setGmt_modified(Timestamp.valueOf(simpleDate.format(date)));
            userMapper.insert(saveUser);
            return Result.success();
        }
    }

    @PostMapping("/setAdmin")
    public Result register(@RequestParam Integer id){
        return Result.success(userMapper.setAdmin(id));
    }



    //查询所有数据
    @GetMapping()
    public Result findAll() {return Result.success(userService.list());}

}

