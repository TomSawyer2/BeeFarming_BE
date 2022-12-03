package com.bf.modules.batchTasks.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RunBatchTasksDto {
    private String name;

    @NotBlank
    private Integer codeIdAHoney;

    @NotBlank
    private Integer codeIdAHornet;

    @NotBlank
    private Integer codeIdBHoney;

    @NotBlank
    private Integer codeIdBHornet;

    @NotBlank
    private Integer totalRounds;

    private Integer timeout;
}