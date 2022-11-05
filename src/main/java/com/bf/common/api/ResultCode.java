package com.bf.common.api;

public enum ResultCode implements ErrorCode {
    FAILED(-1, "操作失败"),
    SUCCESS(0, "操作成功"),
    USER_EXISTS(1, "用户已存在"),
    PWD_ERR(2, "账号密码错误"),
    USER_NOT_EXIST(3, "用户不存在"),
    TAG_EXISTED(11, "tag已存在"),
    ARTICLE_DEL_ERR(6, "文章删除失败"),
    UPDATE_ERR(9, "文章更新失败"),
    GET_ARTICLE_ERR(13, "查询失败"),
    TAG_GET_ART_ERR(14, "用tag查找文章失败"),
    MSG_NOT_FOUND(21, "未找到消息"),
    NEED_LOGIN(100, "需要登录"),
    VALIDATE_FAILED(500, "参数验证失败"),
    UNAUTHORIZED(501, "未授权"),
    FORBIDDEN(502, "未授权"),
    TOKEN_MISSING(503, "缺少token或token已过期");

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
