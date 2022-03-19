package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@TableName(value="item")
public class Item {

    @TableId(value = "id",type = IdType.AUTO)
    private BigInteger id;
    private String name;
    private String introduction;
    private String photo;
    private BigDecimal price;
    private Integer number;
    private BigInteger seller_id;
    private boolean is_audit;
    private boolean is_deleted;
    private Timestamp gmt_create;
    private Timestamp gmt_modified;
}
