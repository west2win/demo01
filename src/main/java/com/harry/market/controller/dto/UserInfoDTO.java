package com.harry.market.controller.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    //返回给前端的用户信息
    private String head;
    private String nickName;
    private String tel;
    private String qq;
    private Integer age;
    private String intro;
}
