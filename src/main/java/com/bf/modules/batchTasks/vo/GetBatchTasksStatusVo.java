package com.bf.modules.batchTasks.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetBatchTasksStatusVo {
    @NotBlank
    private Integer batchTaskId;

    @NotBlank
    private Integer status;

    @NotBlank
    private java.util.Date startTime;

    @NotBlank
    private Integer totalRounds;

    @NotBlank
    private Integer currentRound;

    private String name;

    @NotBlank
    private Integer timeout;

    @NotBlank
    private String containerId;

    @NotBlank
    private Integer codeIdAHoney;

    @NotBlank
    private Integer codeIdAHornet;

    @NotBlank
    private Integer codeIdBHoney;

    @NotBlank
    private Integer codeIdBHornet;

}