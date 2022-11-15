package com.bf.common.enums;

public enum BatchTaskStatus {
    WAITING(0, "未知状态"),
    RUNNING(1, "运行中"),
    FINISHED(2, "已完成"),
    FAILED(3, "失败"),
    TIMEOUT(4, "超时"),
    STOPPED(5, "已停止");

    private final int code;
    private final String description;

    BatchTaskStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
