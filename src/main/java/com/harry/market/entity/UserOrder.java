package com.harry.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName(value="user_order")
public class UserOrder {

    @TableId(value="id",type = IdType.ASSIGN_ID)
    private Long id;
    private Long user_id;
    private Long item_id;
    @TableLogic
    private boolean is_deleted;
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;
}
