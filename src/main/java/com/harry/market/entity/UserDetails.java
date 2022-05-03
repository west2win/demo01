package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName(value="user_details")
public class UserDetails {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String username;
    private String realname;
    private String nickname;
    private String head;
    private Integer gender;
    private Integer age;
    private String tel;
    private Integer qq;
    private String introduction;
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;
}
