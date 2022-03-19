package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private boolean is_deleted;
    private Timestamp gmt_create;
    private Timestamp gmt_modified;
}
