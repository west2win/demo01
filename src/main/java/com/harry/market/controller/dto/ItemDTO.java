package com.harry.market.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class ItemDTO {
    private BigInteger id;
    private String kind;
    private String name;
    private String sellerName;
    private String introduction;
    private String photo;
    private BigDecimal price;
    private Date gmt_modified;
}
