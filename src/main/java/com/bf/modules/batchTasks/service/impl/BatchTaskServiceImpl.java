package com.bf.modules.batchTasks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.common.api.ResultCode;
import com.bf.common.docker.MyDockerClient;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

@Service
public class BatchTaskServiceImpl extends ServiceImpl<BatchTaskMapper, BatchTask> implements BatchTaskService {

    @Autowired
    BatchTaskMapper batchTaskMapper;

    @Autowired
    CodeMapper codeMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private MyDockerClient myDockerClient;

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
        Code codeAHoney = codeMapper.selectById(runBatchTasksDto.getCodeIdAHoney());
        Code codeAHornet = codeMapper.selectById(runBatchTasksDto.getCodeIdAHornet());
        Code codeBHoney = codeMapper.selectById(runBatchTasksDto.getCodeIdBHoney());
        Code codeBHornet = codeMapper.selectById(runBatchTasksDto.getCodeIdBHornet());
        User currentUser = AuthInterceptor.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        if (codeAHoney == null || codeAHornet == null || codeBHoney == null || codeBHornet == null) {
            Asserts.fail(ResultCode.CODE_NOT_EXIST);
        } else if (codeAHoney.getUserId() != currentUserId || codeAHornet.getUserId() != currentUserId || codeBHoney.getUserId() != currentUserId || codeBHornet.getUserId() != currentUserId) {
            Asserts.fail(ResultCode.CODE_NOT_BELONG_TO_USER);
        } else if (!"honey-A".equals(codeAHoney.getType()) || !"hornet-A".equals(codeAHornet.getType()) || !"honey-B".equals(codeBHoney.getType()) || !"hornet-B".equals(codeBHornet.getType()) ) {
            Asserts.fail(ResultCode.CODE_NOT_CORRESPOND);
        }

        batchTask.setUserId(currentUserId);
        batchTask.setStatus(BatchTaskStatus.RUNNING.getCode());
        batchTask.setStartTime(new Date());
        batchTaskMapper.insert(batchTask);

        // 寻找codeFiles文件夹，如果没有就创建
        String codeFilesPath = "codeFiles";
        File codeFiles = new File(codeFilesPath);
        if (!codeFiles.exists()) {
            codeFiles.mkdir();
        }
        // 进入codeFiles文件夹，查找id对应的文件夹，如果没有就创建
        String codeFilesIdPath = codeFilesPath + "/" + batchTask.getId();
        File codeFilesId = new File(codeFilesIdPath);
        if (!codeFilesId.exists()) {
            codeFilesId.mkdir();
        }
        // 将codeAHoney的保存为文件"codeAHoney.java"，放在codeFiles-${id}文件夹中
        String codeAHoneyPath = codeFilesIdPath + "/codeAHoney.java";
        try (FileWriter writer = new FileWriter(codeAHoneyPath)) {
            writer.write(codeAHoney.getContent());
        } catch(IOException e){
            Asserts.fail(ResultCode.CODE_SAVE_ERR);
        }
        String codeAHornetPath = codeFilesIdPath + "/codeAHornet.java";
        try (FileWriter writer = new FileWriter(codeAHornetPath)) {
            writer.write(codeAHornet.getContent());
        } catch(IOException e){
            Asserts.fail(ResultCode.CODE_SAVE_ERR);
        }
        String codeBHoneyPath = codeFilesIdPath + "/codeBHoney.java";
        try (FileWriter writer = new FileWriter(codeBHoneyPath)) {
            writer.write(codeBHoney.getContent());
        } catch(IOException e){
            Asserts.fail(ResultCode.CODE_SAVE_ERR);
        }
        String codeBHornetPath = codeFilesIdPath + "/codeBHornet.java";
        try (FileWriter writer = new FileWriter(codeBHornetPath)) {
            writer.write(codeBHornet.getContent());
        } catch(IOException e){
            Asserts.fail(ResultCode.CODE_SAVE_ERR);
        }

        myDockerClient.tryCreateServerContainer(batchTask, "output-upper", "output-down");
        myDockerClient.startContainer(batchTask.getContainerId());

        try {
            monitorContainer(batchTask);
        } catch (Exception e) {
            System.out.println(e);
        }

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

    @Async
    public void monitorContainer(BatchTask batchTask) {
        String status = "";
        String exitCode = "";
        boolean isExited = false;
        long begin = System.currentTimeMillis();

        try {
            while (true) {
                status = myDockerClient.inspectStatus(batchTask.getContainerId());
                isExited = "'exited'".equals(status);
                if (isExited) {
                    exitCode = myDockerClient.inspectExitCode(batchTask.getContainerId());
                    break;
                } else {
                    long cur = System.currentTimeMillis();
                    if (cur - begin > batchTask.getTimeout() * 60 * 1000) {
                        myDockerClient.stopContainer(batchTask.getContainerId());
                        batchTask.setEndTime(new Date());
                        batchTask.setStatus(BatchTaskStatus.TIMEOUT.getCode());
                        batchTaskMapper.updateById(batchTask);
                    }
                    Thread.sleep(1000);
                }
            }
            if (isExited) {
                if ("'0'".equals(exitCode)) {
                    // todo 分析/codeFiles/{id}/Result里面的两个文件
                } else {
                    // 报错退出处理
                    batchTask.setEndTime(new Date());
                    batchTask.setStatus(BatchTaskStatus.FAILED.getCode());
                    batchTaskMapper.updateById(batchTask);
                }
            } else {
                // 报错退出处理
                batchTask.setEndTime(new Date());
                batchTask.setStatus(BatchTaskStatus.FAILED.getCode());
                batchTaskMapper.updateById(batchTask);
            }
        } catch(Exception e) {
            batchTask.setEndTime(new Date());
            batchTask.setStatus(BatchTaskStatus.FAILED.getCode());
            batchTaskMapper.updateById(batchTask);
        }
    }
}
