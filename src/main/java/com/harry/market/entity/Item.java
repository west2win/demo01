package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName(value="item")
public class Item {

    @TableId(value = "id",type = IdType.AUTO)
    private BigInteger id;
    private String kind;
    @TableField(condition = SqlCondition.LIKE)
    private String name;
    private String introduction;
    private String photo;
    private BigDecimal price;
    private Integer number;
    private BigInteger seller_id;
    private boolean is_audit;
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;
}
