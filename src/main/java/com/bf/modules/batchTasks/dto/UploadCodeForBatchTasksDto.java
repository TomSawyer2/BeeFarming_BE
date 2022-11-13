package com.bf.modules.batchTasks.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UploadCodeForBatchTasksDto {
    @NotBlank
    private String content;

    @NotBlank
    private String type;

    private Integer codeId;
}