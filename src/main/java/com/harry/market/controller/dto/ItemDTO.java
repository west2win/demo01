package com.harry.market.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    private Long itemId;
    private String kind;
    private String name;
    private String intro;
    private BigDecimal price;
    private Integer number;
    private Long seller_id;
}
