package com.harry.market.exception;

import lombok.Getter;

/**
 * @author HarryGao
 */

@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public ServiceException(String code,String msg){
        super(msg);
        this.code = code;
    }
}
