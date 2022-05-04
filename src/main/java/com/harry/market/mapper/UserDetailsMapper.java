package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.controller.dto.UserInfoVO;
import com.harry.market.entity.UserDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDetailsMapper extends BaseMapper<UserDetails> {
    @Select("select nickname,head,age,tel,qq,introduction from user_details where id = #{id}")
    UserInfoVO getUserInfo(String id);
}
