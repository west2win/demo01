package com.harry.market.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.harry.market.common.Constants;
import com.harry.market.common.Result;
import com.harry.market.controller.dto.*;
import com.harry.market.controller.vo.UserInfoVO;
import com.harry.market.entity.BuyerMsg;
import com.harry.market.entity.User;
import com.harry.market.entity.UserDetails;
import com.harry.market.mapper.BuyerMsgMapper;
import com.harry.market.mapper.UserDetailsMapper;
import com.harry.market.mapper.UserMapper;
import com.harry.market.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


/**
 * @authors 222100209_李炎东
 */
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

    @Autowired
    private BuyerMsgMapper buyerMsgMapper;

    /**
     * @authors HarryGao
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录接口")
    public Result login(@RequestBody UserDTO userDTO) {
//        String username = userDTO.getUsername();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        List<User> us = userMapper.sameEmail(email);
        if (us.isEmpty()) {
            return Result.error(Constants.CODE_400,"该邮箱未被注册");
        }
        String username = us.get(0).getUsername();
        userDTO.setUsername(username);
        if (StrUtil.isBlank(email) || StrUtil.isBlank(password) || StrUtil.isBlank(username)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }else if(userMapper.sameName(username).get(0).getPerm().equals("管理员")){
            return Result.adminSuccess();
        }else{
            UserDTO login = userService.login(userDTO);
//            return Result.success(userDTO.getId());
            return Result.success(userService.getUserInfo(login));
        }

    }

    /**
     * @author HarryGao
     * @param userRegDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("注册接口")
    public Result register(@RequestBody UserRegDTO userRegDTO) {
        String username = userRegDTO.getUsername();
//        String password = Md5Utils.code(userDTO.getPassword());

        // 改成强hash加密 by lyd
        String password = userRegDTO.getPassword();
        password = encoder.encode(password);
        userRegDTO.setPassword(password);

        String email = userRegDTO.getEmail();

        if (StrUtil.isBlank(email) || StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }else if(userMapper.sameName(username).size()!=0){
            return Result.error(Constants.CODE_400, "此用户名已被注册");
        }else if(userMapper.sameEmail(email).size()!=0) {
            return Result.error(Constants.CODE_400,"此邮箱已被注册");
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
            BuyerMsg buyerMsg = new BuyerMsg();
            buyerMsg.setId(id);
            buyerMsgMapper.updateById(buyerMsg);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(saveUser.getUsername());
            userDTO.setPassword(saveUser.getPassword());
            return Result.success(userService.getUserInfo(userDTO));
        }
    }

    /**
     * @author HarryGao
     * @param id
     * @return
     */
    @PostMapping("/setAdmin")
    @ApiOperation("设置为管理员")
    public Result register(@RequestParam Integer id){
        return Result.success(userMapper.setAdmin(id));
    }

    /**
     * @author HarryGao
     * @return
     */
    //查询所有数据
    @GetMapping()
    @ApiOperation("获取所有数据")
    public Result findAll() {return Result.success(userService.list());}

    /**
     * @author 222100209_李炎东
     * @param id 用户id
     * @return
     */
    //获取用户信息(头像、昵称、联系方式等)
    @GetMapping("/get")
    @ApiOperation("获取用户信息")
    public Result getUserInfo(@RequestParam Long id) {
        UserInfoVO userInfo = userService.getUserInfoById(id);
        return Result.success(userInfo);
    }

    /**
     * @author 222100209_李炎东
     * @param chgPwdDTO
     * @return
     */
    @PostMapping("/chgPwd")
    @ApiOperation("修改密码")
    public Result changePassword(@RequestBody ChgPwdDTO chgPwdDTO) {
        List<User> us = userMapper.sameEmail(chgPwdDTO.getEmail());
        if (us.isEmpty()) {
            return Result.error(Constants.CODE_400,"该邮箱未被注册");
        }
//        String password = us.get(0).getPassword();
//        if (password != null && !encoder.matches(chgPwdDTO.getOldPassword(),password)) {
//            return Result.error(Constants.CODE_400,"原密码错误");
//        }
        userService.changePassword(chgPwdDTO);
        return Result.success(userService.getUserById(userService.getUserbyEmail(chgPwdDTO.getEmail()).getId()));
    }

    /**
     * @author 222100209_李炎东
     * @param chgUserInfoDTO
     * @return
     */
    @PostMapping("/chgUInfo")
    @ApiOperation("修改用户信息")
    public Result changeUserInfo(@RequestBody ChgUserInfoDTO chgUserInfoDTO) {
        userService.changeUserInfo(chgUserInfoDTO);
        return Result.success(userService.getUserInfoById(chgUserInfoDTO.getUserId()));
    }

}

