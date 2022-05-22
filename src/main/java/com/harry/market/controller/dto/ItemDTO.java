package com.harry.market.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    @JsonIgnore
    private Long itemId;
    @JsonIgnore
    private String photo;

    private String kind;
    private String name;
    private String intro;
    private BigDecimal price;
    private Integer number;
    private Long seller_id;
}
