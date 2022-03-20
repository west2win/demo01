package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
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
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp gmt_create;
    @TableField(fill = FieldFill.UPDATE)
    private Timestamp gmt_modified;
}
