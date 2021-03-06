package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName(value="`order`")
public class Order {
    @TableId(value="id",type = IdType.ASSIGN_ID)
    private Long id;
    private Long item_id;
    private Long seller_id;
    private Integer number;
    private BigDecimal per_price;
    private boolean status;
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;
}
