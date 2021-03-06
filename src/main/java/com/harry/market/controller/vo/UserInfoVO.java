package com.harry.market.controller.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    //返回给前端的用户信息
    private String head;
    private String nickName;
    private String tel;
    private String qq;
    private Integer age;
    private String intro;

    private String address;
    private String gender;
}
