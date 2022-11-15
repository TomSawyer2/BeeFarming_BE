package com.bf.modules.batchTasks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.common.api.ResultCode;
import com.bf.common.enums.BatchTaskStatus;
import com.bf.common.exception.Asserts;
import com.bf.common.interceptor.AuthInterceptor;
import com.bf.common.util.JwtUtils;
import com.bf.modules.batchTasks.dto.RunBatchTasksDto;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.mapper.BatchTaskMapper;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.service.BatchTaskService;
import com.bf.modules.batchTasks.vo.GetBatchTasksStatusVo;
import com.bf.modules.batchTasks.vo.RunBatchTasksVo;
import com.bf.modules.batchTasks.vo.StopBatchTaskVo;
import com.bf.modules.batchTasks.vo.UploadCodeForBatchTasksVo;
import com.bf.modules.code.mapper.CodeMapper;
import com.bf.modules.code.model.Code;
import com.bf.modules.user.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BatchTaskServiceImpl extends ServiceImpl<BatchTaskMapper, BatchTask> implements BatchTaskService {

    @Autowired
    BatchTaskMapper batchTaskMapper;

    @Autowired
    CodeMapper codeMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public UploadCodeForBatchTasksVo uploadCode(UploadCodeForBatchTasksDto uploadCodeForBatchTasksDto) {
        // 如果有codeId，说明是更新代码
        if (uploadCodeForBatchTasksDto.getCodeId() != null) {
            Code code = codeMapper.selectById(uploadCodeForBatchTasksDto.getCodeId());
            if (code == null) Asserts.fail(ResultCode.CODE_NOT_EXIST);
            code.setContent(uploadCodeForBatchTasksDto.getContent());
            codeMapper.updateById(code);
            UploadCodeForBatchTasksVo res = new UploadCodeForBatchTasksVo();
            res.setCodeId(code.getId());
            return res;
        } else {
            // 如果没有codeId，说明是上传代码
            Code code = new Code();
            code.setContent(uploadCodeForBatchTasksDto.getContent());
            // 获取当前用户
            User currentUser = AuthInterceptor.getCurrentUser();
            Integer currentUserId = currentUser.getId();
            code.setUserId(currentUserId);
            BeanUtils.copyProperties(uploadCodeForBatchTasksDto, code);
            codeMapper.insert(code);
            UploadCodeForBatchTasksVo res = new UploadCodeForBatchTasksVo();
            res.setCodeId(code.getId());
            return res;
        }
    }

    @Override
    public RunBatchTasksVo runBatchTasks(RunBatchTasksDto runBatchTasksDto) {
        BatchTask batchTask = new BatchTask();
        BeanUtils.copyProperties(runBatchTasksDto, batchTask);
        User currentUser = AuthInterceptor.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        batchTask.setUserId(currentUserId);
        batchTask.setStatus(BatchTaskStatus.RUNNING.getCode());
        batchTask.setStartTime(new Date());
        batchTaskMapper.insert(batchTask);
        RunBatchTasksVo res = new RunBatchTasksVo();
        res.setBatchTaskId(batchTask.getId());
        res.setTotalRounds(batchTask.getTotalRounds());
        return res;
    }

    @Override
    public GetBatchTasksStatusVo getBatchTasksStatus(Integer batchTaskId) {
        BatchTask batchTask = batchTaskMapper.selectById(batchTaskId);
        if (batchTask == null) Asserts.fail(ResultCode.BATCH_TASK_NOT_EXIST);
        GetBatchTasksStatusVo res = new GetBatchTasksStatusVo();
        BeanUtils.copyProperties(batchTask, res);
        res.setBatchTaskId(batchTaskId);
        return res;
    }

    @Override
    public StopBatchTaskVo stopBatchTask(Integer batchTaskId) {
        BatchTask batchTask = batchTaskMapper.selectById(batchTaskId);
        if (batchTask == null) Asserts.fail(ResultCode.BATCH_TASK_NOT_EXIST);
        if (batchTask.getStatus() != BatchTaskStatus.RUNNING.getCode()) Asserts.fail(ResultCode.BATCH_TASK_NOT_RUNNING);
        batchTask.setStatus(BatchTaskStatus.STOPPED.getCode());
        batchTask.setEndTime(new Date());
        batchTaskMapper.updateById(batchTask);
        StopBatchTaskVo res = new StopBatchTaskVo();
        res.setBatchTaskId(batchTaskId);
        res.setStatus(BatchTaskStatus.STOPPED.getCode());
        return res;
    }
}
