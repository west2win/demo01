package com.harry.market.controller.dto;

import lombok.Data;

@Data
public class ChgPwdDTO {
    private Long userId;
    private String newPassword;
}
