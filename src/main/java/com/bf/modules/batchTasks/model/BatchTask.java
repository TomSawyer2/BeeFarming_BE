package com.bf.modules.batchTasks.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("batch_tasks")
public class BatchTask {
    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    private String name;

    private Integer userId;

    private Integer status;

    private String containerId;

    private Integer codeIdAHoney;

    private Integer codeIdAHornet;

    private Integer codeIdBHoney;

    private Integer codeIdBHornet;

    private String upperGoals;

    private String lowerGoals;

    private Integer totalRounds;

    private Integer currentRound;

    private java.util.Date startTime;

    private java.util.Date endTime;

    private Integer timeout;

    private String containerLog;

    private Double confidenceLevel;
}
