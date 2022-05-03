package com.harry.market.controller.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class OrderDTO {
    private Long buyer_id;
    private Long good_id;
    private Integer number;

}
