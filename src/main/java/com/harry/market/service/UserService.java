package com.harry.market.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.common.Constants;
import com.harry.market.controller.dto.UserDTO;
import com.harry.market.entity.User;
import com.harry.market.exception.ServiceException;
import com.harry.market.mapper.UserMapper;
import com.harry.market.utils.Md5Utils;
import org.springframework.stereotype.Service;

/**
 * @program: market
 * @author: HarryGao
 * @create: 2022-03-19 14:20
 */

@Service
public class UserService extends ServiceImpl<UserMapper,User> {

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
}
