package com.harry.market.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.common.Constants;
import com.harry.market.controller.UserController;
import com.harry.market.controller.dto.UserDTO;
import com.harry.market.controller.dto.UserInfoDTO;
import com.harry.market.entity.User;
import com.harry.market.entity.UserDetails;
import com.harry.market.exception.ServiceException;
import com.harry.market.mapper.UserDetailsMapper;
import com.harry.market.mapper.UserMapper;
import com.harry.market.utils.ExcelUtill;
import com.harry.market.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;


@Service
public class UserService extends ServiceImpl<UserMapper,User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private UserController userController;

    private static final Log LOG = Log.get();

    public UserDTO login(UserDTO userDTO) {
        userDTO.setPassword(Md5Utils.code(userDTO.getPassword()));
        User one = getUserInfo(userDTO);

        if(one !=null){
            BeanUtil.copyProperties(one,userDTO,true);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    private User getUserInfo(UserDTO userDTO){
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

    public BigInteger getUserId(String username) {
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
            userDetailsMapper.insert(user);
        }

    }

    public UserInfoDTO getUserInfo() {
        String username;
        //从springsecurity中取出当前用户
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userDetailsMapper.getUserInfo(username);
    }

}
