package com.bf.modules.batchTasks.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RunBatchTasksVo {
    @NotBlank
    private Integer batchTaskId;

    @NotBlank
    private Integer totalRounds;
}
