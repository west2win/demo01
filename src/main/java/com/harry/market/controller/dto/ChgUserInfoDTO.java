package com.harry.market.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChgUserInfoDTO {
    private Long userId;
    private String realname;
    private String nickname;
    @ApiModelProperty(notes = "性别(0不详，1男，2女)")
    private Integer gender;
    private Integer age;
    private String address;
    private String tel;
    private String email;
    private Integer qq;
    private String intro;
}
