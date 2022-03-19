package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private boolean is_deleted;
    private Timestamp gmt_create;
    private Timestamp gmt_modified;
}
