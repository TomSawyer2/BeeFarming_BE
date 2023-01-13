package com.bf.modules.batchTasks.vo;

import com.bf.modules.batchTasks.model.BatchTask;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class GetBatchTasksHistoryVo {
    @NotBlank
    private Integer page;

    @NotBlank
    private Integer pageSize;

    @NotBlank
    private Integer total;

    private List<BatchTask> batchTasks;

}