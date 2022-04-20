package com.harry.market.controller.dto;

import lombok.Data;

@Data
public class UserDTO {
    //接收前端登录请求的参数

        private Integer id;
        private String username;
        private String password;

}
