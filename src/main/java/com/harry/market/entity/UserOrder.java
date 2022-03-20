package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@TableName(value="user_order")
public class UserOrder {

    @TableId(value = "id",type = IdType.AUTO)
    private BigInteger id;
    private BigInteger user_id;
    private BigInteger order_id;
    @TableLogic
    private Integer is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp gmt_create;
    @TableField(fill = FieldFill.UPDATE)
    private Timestamp gmt_modified;
}
