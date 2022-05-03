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

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    private String kind;
    private String name;
    private String introduction;
    private String photo;
    private BigDecimal price;
    private Integer number;
    private Long seller_id;
    private boolean is_audit;
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;
}
