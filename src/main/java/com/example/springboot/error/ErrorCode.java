package com.example.springboot.error;

public enum ErrorCode {
    REQUEST_PARAM_ERROR(500, "请求参数不正确"),
    USER_ALREADY_EXISTS(500, "该账号已经存在"),
    USER_TOKEN_NOT_EXISTS(403, "token不存在"),
    USER_ROLE_REFUSED(401, "无权限访问"),
    USER_NOT_EXISTS(403, "用户不存在, 请重新登录"),
    USER_PASSWORD_ERROR(400, "账号或密码错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
