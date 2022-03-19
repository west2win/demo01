package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName(value="user_detail")
public class UserDetails {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String nickname;
    private String head;
    private Integer gender;
    private Integer age;
    private Integer tel;
    private Integer qq;
    private String introduction;
    private Integer is_deleted;
    private Timestamp gmt_create;
    private Timestamp gmt_modified;
}
