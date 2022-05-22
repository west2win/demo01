package com.harry.market.controller.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class UserVO {
    private String id;
    private String username;
    private String password;
    private String perm;
    private boolean is_deleted;
    private Date gmt_create;
    private Date gmt_modified;
}
