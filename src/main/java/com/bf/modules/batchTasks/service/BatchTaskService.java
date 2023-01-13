package com.bf.modules.batchTasks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.modules.admin.vo.GetCodeForAdminVo;
import com.bf.modules.batchTasks.dto.RunBatchTasksDto;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.vo.*;
import com.bf.modules.code.model.Code;

import java.util.List;

public interface BatchTaskService extends IService<BatchTask> {
    public UploadCodeForBatchTasksVo uploadCode(UploadCodeForBatchTasksDto uploadCodeForBatchTasksDto);
    public BatchTask runBatchTasks(RunBatchTasksDto runBatchTasksDto);
    public GetBatchTasksStatusVo getBatchTasksStatus(Integer batchTaskId);
    public StopBatchTaskVo stopBatchTask(Integer batchTaskId);
    public void monitorContainer(BatchTask batchTask);
    public GetBatchTasksResultVo getBatchTasksResult(int id);
    public GetBatchTasksHistoryVo getBatchTasksHistory(int page, int pageSize);
    public GetCodeForAdminVo getCodeByUser(int page, int pageSize);
    public List<Code> getCodesByIds(List<Integer> ids);
}
