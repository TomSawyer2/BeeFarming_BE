package com.bf.modules.batchTasks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.common.api.ResultCode;
import com.bf.common.docker.MyDockerClient;
import com.bf.common.enums.BatchTaskStatus;
import com.bf.common.enums.UserStatus;
import com.bf.common.exception.Asserts;
import com.bf.common.interceptor.AuthInterceptor;
import com.bf.common.service.RedisService;
import com.bf.common.util.JwtUtils;
import com.bf.modules.admin.vo.GetCodeForAdminVo;
import com.bf.modules.batchTasks.dto.RunBatchTasksDto;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.mapper.BatchTaskMapper;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.service.BatchTaskService;
import com.bf.modules.batchTasks.vo.*;
import com.bf.modules.code.mapper.CodeMapper;
import com.bf.modules.code.model.Code;
import com.bf.modules.user.mapper.UserMapper;
import com.bf.modules.user.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class BatchTaskServiceImpl extends ServiceImpl<BatchTaskMapper, BatchTask> implements BatchTaskService {

    @Autowired
    BatchTaskMapper batchTaskMapper;

    @Autowired
    CodeMapper codeMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RedisService redisService;

    @Autowired
    private MyDockerClient myDockerClient;

    private static final Logger log = LoggerFactory.getLogger(MyDockerClient.class);

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
    public BatchTask runBatchTasks(RunBatchTasksDto runBatchTasksDto) {
        User currentUser = AuthInterceptor.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        // select * from batch_task where user_id = ? and status = 1
        LambdaQueryWrapper<BatchTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchTask::getUserId, currentUser.getId());
        queryWrapper.eq(BatchTask::getStatus, BatchTaskStatus.RUNNING.getCode());
        List<BatchTask> batchTasks = batchTaskMapper.selectList(queryWrapper);
        if (batchTasks.size() > 0) {
            Asserts.fail(ResultCode.USER_TASK_RUNNING);
        }

        BatchTask batchTask = new BatchTask();
        BeanUtils.copyProperties(runBatchTasksDto, batchTask);
        Code codeAHoney = codeMapper.selectById(runBatchTasksDto.getCodeIdAHoney());
        Code codeAHornet = codeMapper.selectById(runBatchTasksDto.getCodeIdAHornet());
        Code codeBHoney = codeMapper.selectById(runBatchTasksDto.getCodeIdBHoney());
        Code codeBHornet = codeMapper.selectById(runBatchTasksDto.getCodeIdBHornet());

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
        String archiveResultsPath = "archiveResults";
        File archiveResults = new File(archiveResultsPath);
        if (!archiveResults.exists()) {
            archiveResults.mkdir();
        }
        String archiveResultsIdPath = archiveResultsPath + "/" + batchTask.getId();
        File archiveResultsId = new File(archiveResultsIdPath);
        if (!archiveResultsId.exists()) {
            archiveResultsId.mkdir();
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

        String containerId = myDockerClient.tryCreateServerContainer(batchTask, "output-upper", "output-down");
        myDockerClient.startContainer(containerId);
        batchTask.setContainerId(containerId);
        batchTaskMapper.updateById(batchTask);

        currentUser.setStatus(UserStatus.TASK_RUNNING.getCode());
        userMapper.updateById(currentUser);

        return batchTask;
    }

    @Override
    public GetBatchTasksStatusVo getBatchTasksStatus(Integer batchTaskId) {
        BatchTask batchTask = batchTaskMapper.selectById(batchTaskId);
        if (batchTask == null) Asserts.fail(ResultCode.BATCH_TASK_NOT_EXIST);
        if (batchTask.getUserId() != AuthInterceptor.getCurrentUser().getId()) Asserts.fail(ResultCode.BATCH_TASK_NOT_BELONG_TO_USER);
        GetBatchTasksStatusVo res = new GetBatchTasksStatusVo();
        BeanUtils.copyProperties(batchTask, res);
        int currentRound = res.getCurrentRound();
        if (redisService.getValue(String.valueOf(batchTask.getId())) != null) {
            currentRound = Integer.parseInt(redisService.getValue(String.valueOf(batchTask.getId())));
            res.setCurrentRound(currentRound);
        }
        res.setBatchTaskId(batchTaskId);
        return res;
    }

    @Override
    public StopBatchTaskVo stopBatchTask(Integer batchTaskId) {
        BatchTask batchTask = batchTaskMapper.selectById(batchTaskId);
        if (batchTask == null) Asserts.fail(ResultCode.BATCH_TASK_NOT_EXIST);
        if (batchTask.getUserId() != AuthInterceptor.getCurrentUser().getId()) Asserts.fail(ResultCode.BATCH_TASK_NOT_BELONG_TO_USER);
        if (batchTask.getStatus() != BatchTaskStatus.RUNNING.getCode()) Asserts.fail(ResultCode.BATCH_TASK_NOT_RUNNING);
        myDockerClient.stopAndRemoveContainer(batchTask.getContainerId());
        batchTask.setStatus(BatchTaskStatus.FAILED.getCode());
        batchTask.setEndTime(new Date());
        batchTaskMapper.updateById(batchTask);
        StopBatchTaskVo res = new StopBatchTaskVo();
        res.setBatchTaskId(batchTaskId);
        res.setStatus(BatchTaskStatus.FAILED.getCode());
        User currentUser = AuthInterceptor.getCurrentUser();
        currentUser.setStatus(UserStatus.IDLE.getCode());
        userMapper.updateById(currentUser);
        return res;
    }

    @Async
    public void monitorContainer(BatchTask batchTask) {
        String status = "";
        String exitCode = "";
        boolean isExited = false;
        boolean isTimeout = false;
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
                        // 超时逻辑
                        batchTask.setEndTime(new Date());
                        batchTask.setStatus(BatchTaskStatus.TIMEOUT.getCode());
                        isTimeout = true;
                        break;
                    }

                    Thread.sleep(1000);
                }
            }

            if (!isTimeout) {
                if ("'0'".equals(exitCode)) {
                    String upperResultPath = "/home/ubuntu/BF/archiveResults/" + batchTask.getId() + "/output-upper";
                    List<String> linesUpper = Files.readAllLines(Paths.get(upperResultPath), StandardCharsets.UTF_8);
                    String upperResult = String.join(",", linesUpper);
                    String downResultPath = "/home/ubuntu/BF/archiveResults/" + batchTask.getId() + "/output-down";
                    List<String> linesLower = Files.readAllLines(Paths.get(downResultPath), StandardCharsets.UTF_8);
                    String lowerResult = String.join(",", linesLower);

                    batchTask.setUpperGoals(upperResult);
                    batchTask.setLowerGoals(lowerResult);

                    batchTask.setEndTime(new Date());
                    batchTask.setStatus(BatchTaskStatus.FINISHED.getCode());
                } else {
                    // 报错退出处理
                    batchTask.setEndTime(new Date());
                    batchTask.setStatus(BatchTaskStatus.FAILED.getCode());
                }
            }
        } catch(Exception e) {
            batchTask.setEndTime(new Date());
            batchTask.setStatus(BatchTaskStatus.FAILED.getCode());
        }

        int currentRound = 0;
        if (redisService.getValue(String.valueOf(batchTask.getId())) != null) {
            currentRound = Integer.parseInt(redisService.getValue(String.valueOf(batchTask.getId())));
        }
        batchTask.setCurrentRound(currentRound);

        String logs = myDockerClient.execTailContainerLogsCmd(batchTask.getContainerId(), 300);
        batchTask.setContainerLog(logs);

        batchTaskMapper.updateById(batchTask);
        myDockerClient.stopAndRemoveContainer(batchTask.getContainerId());
        User user = userMapper.selectById(AuthInterceptor.getCurrentUser().getId());
        user.setStatus(UserStatus.IDLE.getCode());
        userMapper.updateById(user);
    }

    @Override
    public GetBatchTasksResultVo getBatchTasksResult(int id) {
        BatchTask batchTask = batchTaskMapper.selectById(id);
        if (batchTask == null) Asserts.fail(ResultCode.BATCH_TASK_NOT_EXIST);
        if (batchTask.getUserId() != AuthInterceptor.getCurrentUser().getId()) Asserts.fail(ResultCode.BATCH_TASK_NOT_BELONG_TO_USER);
        if (batchTask.getStatus() != BatchTaskStatus.FINISHED.getCode() && batchTask.getStatus() != BatchTaskStatus.FAILED.getCode()) Asserts.fail(ResultCode.BATCH_TASK_NOT_FINISHED);
        // 如果有结果则通过结果计算置信度
        if (batchTask.getUpperGoals() != null && batchTask.getUpperGoals() != "" && batchTask.getLowerGoals() != null && batchTask.getLowerGoals() != "") {
            String[] upperGoals = batchTask.getUpperGoals().split(",");
            String[] lowerGoals = batchTask.getLowerGoals().split(",");
            // 计算玩家A和B赢的次数
            int winnerA = 0;
            int winnerB = 0;
            for (int i = 0; i < upperGoals.length; i++) {
                String upperGoal = upperGoals[i];
                String lowerGoal = lowerGoals[i];
                if (Integer.parseInt(upperGoal.split(" ")[0]) > Integer.parseInt(lowerGoal.split(" ")[0])) {
                    winnerA ++;
                } else if (Integer.parseInt(upperGoal.split(" ")[0]) < Integer.parseInt(lowerGoal.split(" ")[0])) {
                    winnerB ++;
                }
            }
            // 计算置信度
            Double confidenceLevel = 11.45;
            batchTask.setConfidenceLevel(confidenceLevel);
            batchTaskMapper.updateById(batchTask);
        }
        GetBatchTasksResultVo res = new GetBatchTasksResultVo();
        BeanUtils.copyProperties(batchTask, res);
        return res;
    }

    @Override
    public GetBatchTasksHistoryVo getBatchTasksHistory(int page, int pageSize) {
        Page<BatchTask> batchTaskPage = new Page<>(page, pageSize);
        QueryWrapper<BatchTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", AuthInterceptor.getCurrentUser().getId());
        queryWrapper.orderByDesc("id");
        batchTaskMapper.selectPage(batchTaskPage, queryWrapper);
        List<BatchTask> batchTasks = batchTaskPage.getRecords();
        GetBatchTasksHistoryVo res = new GetBatchTasksHistoryVo();
        res.setPage(page);
        res.setPageSize(pageSize);
        res.setTotal((int) batchTaskPage.getTotal());
        res.setBatchTasks(batchTasks);
        return res;
    }

    @Override
    public GetCodeForAdminVo getCodeByUser(int page, int pageSize) {
        Page<Code> codePage = new Page<>(page, pageSize);
        QueryWrapper<Code> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", AuthInterceptor.getCurrentUser().getId());
        queryWrapper.orderByDesc("id");
        codeMapper.selectPage(codePage, queryWrapper);
        List<Code> codes = codePage.getRecords();
        GetCodeForAdminVo res = new GetCodeForAdminVo();
        res.setPage(page);
        res.setPageSize(pageSize);
        res.setTotal((int) codePage.getTotal());
        res.setCodeList(codes);
        return res;
    }

    @Override
    public List<Code> getCodesByIds(List<Integer> ids) {
        if (ids.size() > 4) {
            // 取前4个
            ids = ids.subList(0, 4);
        }
        List<Code> codes = codeMapper.selectBatchIds(ids);
        // 筛选出当前用户的code
        List<Code> res = new ArrayList<>();
        for (Code code : codes) {
            if (code.getUserId() == AuthInterceptor.getCurrentUser().getId()) {
                res.add(code);
            }
        }
        return res;
    }
}
