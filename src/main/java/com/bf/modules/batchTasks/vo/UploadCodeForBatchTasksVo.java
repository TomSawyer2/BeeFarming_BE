package com.bf.modules.batchTasks.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UploadCodeForBatchTasksVo {
    @NotBlank
    private Integer codeId;

}