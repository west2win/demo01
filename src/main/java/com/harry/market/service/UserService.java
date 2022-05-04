package com.harry.market.service;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.common.Constants;
import com.harry.market.controller.UserController;
import com.harry.market.controller.dto.UserDTO;
import com.harry.market.controller.dto.UserInfoVO;
import com.harry.market.entity.User;
import com.harry.market.entity.UserDetails;
import com.harry.market.exception.ServiceException;
import com.harry.market.mapper.UserDetailsMapper;
import com.harry.market.mapper.UserMapper;
import com.harry.market.utils.ExcelUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService extends ServiceImpl<UserMapper,User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private static final Log LOG = Log.get();

    public UserDTO login(UserDTO userDTO) {
//        userDTO.setPassword(Md5Utils.code(userDTO.getPassword()));
        String password = getUserPwd(userDTO.getUsername());
        if (password != null && encoder.matches(userDTO.getPassword(),password)) {
            userDTO.setPassword(password);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    public User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User one;
        try{
            one=getOne(queryWrapper); //从数据库查询用户信息
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    private String getUserPwd(String loginName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",loginName);
        String password;
        try{
            password=getOne(queryWrapper).getPassword(); //从数据库查询用户信息
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return password;
    }

    public String getUserPerm(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        String perm = userMapper.selectOne(wrapper).getPerm();
        return perm;
    }

    public User getUser(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper);
    }

    public Long getUserId(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper).getId();
    }

    /**
     *
     * @param row 从几行开始输入
     * @param num 输入几条数据
     */
    public void insertExcelUser(int row,int num) {
        ExcelUtill data = new ExcelUtill("D:\\Code\\project\\coporation\\data.xlsx", "data");
        List<UserDetails> users = data.getExcelUser(row, num);
        for (UserDetails user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword("123456");
            userController.register(userDTO);
            user.setId(userService.getUserId(userDTO.getUsername()));
            userDetailsMapper.updateById(user);
        }

    }

//    public UserInfoDTO getUserInfo() {
//        String username;
//        //从springsecurity中取出当前用户
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//
//        return userDetailsMapper.getUserInfo(username);
//    }

    public UserInfoVO getUserInfoById(String id) {
        User one;
        UserInfoVO info;
        try{
            info = userDetailsMapper.getUserInfo(id);
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return info;
    }

}
