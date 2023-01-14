package com.bf.common.enums;

public enum UserStatus {
    IDLE(0, "闲置状态"),
    TASK_RUNNING(1, "任务运行中");

    private final int code;
    private final String description;

    UserStatus(int code, String description) {
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
