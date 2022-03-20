package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@TableName(value="order")
public class Order {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String item;
    private Integer number;
    private BigDecimal per_price;
    private boolean status;
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp gmt_create;
    @TableField(fill = FieldFill.UPDATE)
    private Timestamp gmt_modified;
}
