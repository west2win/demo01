package com.harry.market.controller.vo;

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
    private String sellerName;
    private String sellerHead;
    private String introduction;
    private BigDecimal price;
    private String is_audit;
    private Date gmt_modified;
}
