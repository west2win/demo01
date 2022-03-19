package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: market
 * @author: HarryGao
 * @create: 2022-03-19 14:20
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username}")
    List<User> sameName(String username);

    @Select("select * from user where perm = '管理员' ")
    List<User> findAdmin(Integer id);

    @Select("Update user Set perm ='管理员' Where id = #{id}")
    List<User> setAdmin(Integer id);
}

