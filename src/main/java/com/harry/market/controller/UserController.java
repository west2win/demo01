package com.harry.market.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.*;
import com.harry.market.controller.vo.UserInfoVO;
import com.harry.market.entity.User;
import com.harry.market.entity.UserDetails;
import com.harry.market.mapper.UserDetailsMapper;
import com.harry.market.mapper.UserMapper;
import com.harry.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

//<<<<<<< HEAD
//=======
import javax.annotation.Resource;
//>>>>>>> a83b967 (商品上传)
//import java.sql.Timestamp;


//<<<<<<< HEAD


@ResponseBody
@RestController
@RequestMapping("/user")
@CrossOrigin(origins ="*")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }else if(userMapper.sameName(username).get(0).getPerm().equals("管理员")){
            return Result.adminSuccess();
        }else{
            UserDTO login = userService.login(userDTO);
//            return Result.success(userDTO.getId());
            return Result.success(userService.getUserInfo(login));
        }

    }

    @PostMapping("/register")
    public Result register(@RequestBody UserRegDTO userRegDTO) {
        String username = userRegDTO.getUsername();
//        String password = Md5Utils.code(userDTO.getPassword());

        // 改成强hash加密
        String password = userRegDTO.getPassword();
        password = encoder.encode(password);
        userRegDTO.setPassword(password);

        String email = userRegDTO.getEmail();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }else if(userMapper.sameName(username).size()!=0){
            return Result.error(Constants.CODE_400, "此用户名已被注册");
        }else {
            User saveUser = new User();
            Long id = IdWorker.getId(saveUser);

            saveUser.setUsername(username);
            saveUser.setPassword(password);
            saveUser.setPerm("user");
            saveUser.setId(id);

            userMapper.insert(saveUser);

            UserDetails ud = new UserDetails();
            ud.setId(id);
            ud.setUsername(saveUser.getUsername());
            ud.setEmail(email);
            userDetailsMapper.insert(ud);

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

    //获取用户信息(头像、昵称、联系方式等)
    @GetMapping("/get")
    public Result getUserInfo(@RequestParam Long id) {
        UserInfoVO userInfo = userService.getUserInfoById(id);
        return Result.success(userInfo);
    }

    @PostMapping("/chgPwd")
    public Result changePassword(@RequestBody ChgPwdDTO chgPwdDTO) {
        userService.changePassword(chgPwdDTO);
        return Result.success(userService.getUserById(chgPwdDTO.getUserId()));
    }

    @PostMapping("/chgUInfo")
    public Result changeUserInfo(@RequestBody ChgUserInfoDTO chgUserInfoDTO) {
        userService.changeUserInfo(chgUserInfoDTO);
        return Result.success(userService.getUserInfoById(chgUserInfoDTO.getUserId()));
    }

}

