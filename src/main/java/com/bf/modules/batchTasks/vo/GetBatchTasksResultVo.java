package com.bf.modules.batchTasks.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetBatchTasksResultVo {
    @NotBlank
    private Integer id;

    private String name;

    @NotBlank
    private Integer userId;

    @NotBlank
    private Integer status;

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

    @NotBlank
    private String upperGoals;

    @NotBlank
    private String lowerGoals;

    @NotBlank
    private Integer totalRounds;

    @NotBlank
    private Integer currentRound;

    @NotBlank
    private java.util.Date startTime;

    @NotBlank
    private java.util.Date endTime;

    @NotBlank
    private Integer timeout;

    private String containerLog;

    private Double confidenceLevel;

}