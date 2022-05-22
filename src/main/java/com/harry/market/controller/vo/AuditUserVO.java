package com.harry.market.controller.vo;

import lombok.Data;

@Data
public class AuditUserVO {
    private String username;
    private String email;
    private String head;
    private Integer releasedNum;
    private Integer unpassedNum;
}
