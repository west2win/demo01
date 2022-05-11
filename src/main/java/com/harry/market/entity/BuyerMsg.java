package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
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
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;
}
