package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value="buyer_msg")
public class BuyerMsg {
    //用户id
    private Long id;
    private Long order_id;
    private String nickname;
    private String tel;
    private String address;
    @TableLogic
    @JsonIgnore
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Date gmt_create;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonIgnore
    private Date gmt_modified;
}
