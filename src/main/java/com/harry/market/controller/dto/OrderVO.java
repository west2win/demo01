package com.harry.market.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVO {
    private String buyerHead;
    private String buyerName;
    private String sellerHead;
    private String sellerName;
    private String itemName;
    private String itemPhoto;
    private BigDecimal price;
    private Integer number;
}
