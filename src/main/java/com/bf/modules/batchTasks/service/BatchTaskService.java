package com.bf.modules.batchTasks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.modules.batchTasks.dto.RunBatchTasksDto;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.vo.GetBatchTasksResultVo;
import com.bf.modules.batchTasks.vo.GetBatchTasksStatusVo;
import com.bf.modules.batchTasks.vo.StopBatchTaskVo;
import com.bf.modules.batchTasks.vo.UploadCodeForBatchTasksVo;

public interface BatchTaskService extends IService<BatchTask> {
    public UploadCodeForBatchTasksVo uploadCode(UploadCodeForBatchTasksDto uploadCodeForBatchTasksDto);
    public BatchTask runBatchTasks(RunBatchTasksDto runBatchTasksDto);
    public GetBatchTasksStatusVo getBatchTasksStatus(Integer batchTaskId);
    public StopBatchTaskVo stopBatchTask(Integer batchTaskId);
    public void monitorContainer(BatchTask batchTask);
    public GetBatchTasksResultVo getBatchTasksResult(int id);
}
