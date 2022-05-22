package com.harry.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harry.market.controller.vo.AuditUserVO;
import com.harry.market.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username}")
    List<User> sameName(String username);
    @Select("select user.* from user_details inner join user on user.id=user_details.id where email = #{email}")
    List<User> sameEmail(String email);

    @Update("Update user Set perm ='管理员' Where id = #{id}")
    Boolean setAdmin(Integer id);

    @Select("select `id` from `user` where `username`=#{username}")
    String getUserId(String username);

//    @Select("select * from `user` where `is_deleted` = 0 limit #{pageNum},#{pageSize}")
    @Select("select nickname username,email,head,(select count(*) from item where seller_id=user_details.id) releasedNum,(select count(*) from item where seller_id=user_details.id and is_audit=1) unpassedNum from user_details order by ${sort} ${order} limit #{pageNum},#{pageSize}")
    List<AuditUserVO> getAllUser(int pageNum, int pageSize, String sort, String order);

    @Select("select count(*) from `user` where `is_deleted` = 0")
    Long getUserCount();

}

