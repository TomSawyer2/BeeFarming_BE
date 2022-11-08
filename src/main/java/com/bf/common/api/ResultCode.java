package com.bf.common.api;

public enum ResultCode implements ErrorCode {
    FAILED(-1, "操作失败"),
    SUCCESS(0, "操作成功"),
    USERNAME_EXIST(1, "用户名已存在"),
    USER_NOT_EXIST(2, "用户不存在"),
    PWD_ERR(3, "密码错误"),
    VALIDATE_FAILED(401, "验证失败"),
    UNAUTHORIZED(402, "未授权"),
    FORBIDDEN(403, "Forbidden"),
    TOKEN_MISSING(404, "未检测到token");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
