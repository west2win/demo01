package com.harry.market.controller.vo;

import com.google.common.primitives.UnsignedLong;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVO {
    private String orderId;
    private String itemId;
    private String buyerHead;
    private String buyerName;
    private String sellerHead;
    private String sellerName;
    private String itemName;
    private String itemPhoto;
    private BigDecimal price;
    private Integer number;
    private String status;
    private String date;
}
