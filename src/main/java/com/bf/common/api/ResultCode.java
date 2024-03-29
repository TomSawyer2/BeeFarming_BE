package com.bf.common.api;

public enum ResultCode implements ErrorCode {
    FAILED(-1, "操作失败"),
    SUCCESS(0, "操作成功"),
    USERNAME_EXIST(1, "用户名已存在"),
    USER_NOT_EXIST(2, "用户不存在"),
    PWD_ERR(3, "密码错误"),
    USER_BANNED(4, "当前用户已被封禁"),
    PERMISSION_DENIED(5, "权限不足"),
    CODE_NOT_EXIST(101, "代码不存在"),
    BATCH_TASK_NOT_EXIST(102, "批处理任务不存在"),
    BATCH_TASK_NOT_RUNNING(103, "批处理任务未运行"),
    CODE_NOT_CORRESPOND(104, "代码ID与代码类型不匹配"),
    CODE_NOT_BELONG_TO_USER(105, "代码不属于当前用户"),
    CODE_SAVE_ERR(106, "代码保存失败"),
    BATCH_TASK_NOT_FINISHED(107, "批处理任务未完成"),
    BATCH_TASK_NOT_BELONG_TO_USER(108, "批处理任务不属于当前用户"),
    USER_TASK_RUNNING(109, "用户已有任务在运行中"),
    VALIDATE_FAILED(1001, "验证失败"),
    UNAUTHORIZED(1002, "未授权"),
    FORBIDDEN(1003, "Forbidden"),
    TOKEN_MISSING(1004, "未检测到token");

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
