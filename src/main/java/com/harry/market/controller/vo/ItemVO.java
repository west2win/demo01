package com.harry.market.controller.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class ItemVO {
    private String id;
    private String kind;
    private String name;
    private String photo;
//    @JsonIgnore
    private String sellerId;
    private String sellerName;
    private String sellerHead;
    private String introduction;
    private BigDecimal price;
    private Integer number;
    private String is_audit;
    private Date gmt_modified;
    //格式化后的日期
    private String sdf;
    private String status;
    private String isSelled;
}
