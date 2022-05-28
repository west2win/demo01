package com.harry.market.controller.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AuditUserVO {
    private String userId;
    private String username;
    private String email;
    private String head;
    private Long releasedNum;
    private Long unpassedNum;
    @JsonIgnore
    private boolean is_deleted;
    private String status;
}
