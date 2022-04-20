package com.harry.market.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;
    private String total;

    public static Result success(){
        return new Result(Constants.CODE_200,"",null,null);
    }

    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data,null);
    }
    public static Result success(Object data,String total) {
        return new Result(Constants.CODE_200,null,data,total);
    }

    public static Result adminSuccess(){
        return new Result(Constants.CODE_222,"已取得管理员权限",null,null);
    }

    public static Result error(String code,String msg){
        return new Result(code,msg,null,null);
    }

    public static Result error(String code401, ResultCode userNotLogin){
        return new Result(Constants.CODE_500,"系统错误",null,null);
    }
}
