package com.bf.modules.batchTasks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.model.BatchTask;

public interface BatchTaskService extends IService<BatchTask> {
    public void uploadCode(UploadCodeForBatchTasksDto uploadCodeForBatchTasksDto);
}
