package com.harry.market.exception;

import com.harry.market.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author HarryGao
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler()
    @ResponseBody
    public Result handle(ServiceException se){
        return Result.error(se.getCode(),se.getMessage());
    }
}
