package com.bf.modules.batchTasks.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TestRedisDto {
    private String key;
}