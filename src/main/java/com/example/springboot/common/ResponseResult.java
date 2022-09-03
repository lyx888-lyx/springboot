package com.example.springboot.common;

import com.example.springboot.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResponseResult {
    private int code;
    private String message;
    private Object data;

    public static ResponseResult success(){
        return success("success");
    }

    public static ResponseResult success(String message){
        return success(message, null);
    }

    public static ResponseResult success(Object data){
        return success(null, data);
    }

    public static ResponseResult success(String message, Object data){
        return new ResponseResult(HttpStatus.SUCCESS, message, data);
    }

    public static ResponseResult error(){
        return error("error");
    }

    public static ResponseResult error(String message){
        return error(message, null);
    }

    public static ResponseResult error(String message, Object data){
        return new ResponseResult(HttpStatus.ERROR, message, data);
    }

    public static ResponseResult error(int code, String message){
        return new ResponseResult(code, message, null);
    }
}
