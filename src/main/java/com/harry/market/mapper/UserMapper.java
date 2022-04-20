package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username}")
    List<User> sameName(String username);

    @Update("Update user Set perm ='管理员' Where id = #{id}")
    Boolean setAdmin(Integer id);

    @Select("select `id` from `user` where `username`=#{username}")
    String getUserId(String username);


}

