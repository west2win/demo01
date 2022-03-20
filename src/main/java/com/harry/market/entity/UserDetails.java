package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
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
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp gmt_create;
    @TableField(fill = FieldFill.UPDATE)
    private Timestamp gmt_modified;
}
