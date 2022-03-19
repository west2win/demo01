package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value="user")
public class User {

    @TableId(value="id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String perm;
    private Integer is_deleted;//0为存在，1为删除
    private Timestamp gmt_create;
    private Timestamp gmt_modified;
}
