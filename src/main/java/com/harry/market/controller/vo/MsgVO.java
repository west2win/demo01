package com.harry.market.controller.vo;

import lombok.Data;

@Data
public class MsgVO {
    private String fromName;
    private String fromHead;
    private String toName;
    private String toHead;
    private String msg;
}
