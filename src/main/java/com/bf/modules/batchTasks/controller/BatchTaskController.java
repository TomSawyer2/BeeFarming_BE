package com.bf.modules.batchTasks.controller;

import com.bf.common.annotation.LoginRequired;
import com.bf.common.api.CommonResult;
import com.bf.common.enums.Permission;
import com.bf.modules.admin.vo.GetCodeForAdminVo;
import com.bf.modules.batchTasks.dto.RunBatchTasksDto;
import com.bf.modules.batchTasks.dto.StopBatchTaskDto;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.service.BatchTaskService;
import com.bf.modules.batchTasks.vo.*;
import com.bf.modules.code.model.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batchTasks")
public class BatchTaskController {
    @Autowired
    BatchTaskService batchTaskService;


    @PostMapping("/uploadCodeForBatchTasks")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult uploadCodeForBatchTasks(@RequestBody UploadCodeForBatchTasksDto uploadCodeForBatchTasksDto) {
        UploadCodeForBatchTasksVo res = batchTaskService.uploadCode(uploadCodeForBatchTasksDto);
        return CommonResult.success(res, "代码上传/更新成功");
    }

    @PostMapping("/run")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult runBatchTasks(@RequestBody RunBatchTasksDto runBatchTasksDto) {
        BatchTask res = batchTaskService.runBatchTasks(runBatchTasksDto);
        batchTaskService.monitorContainer(res);
        return CommonResult.success(res, "开始运行");
    }

    @GetMapping("/status")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult getBatchTasksStatus(@RequestParam Integer batchTaskId) {
        GetBatchTasksStatusVo res = batchTaskService.getBatchTasksStatus(batchTaskId);
        return CommonResult.success(res, "状态查询成功");
    }

    @PostMapping("/stop")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult stopBatchTask(@RequestBody StopBatchTaskDto StopBatchTaskDto) {
        StopBatchTaskVo res = batchTaskService.stopBatchTask(StopBatchTaskDto.getBatchTaskId());
        return CommonResult.success(res, "停止成功");
    }

    @GetMapping("/result")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult getBatchTasksResult(@RequestParam Integer batchTaskId) {
        GetBatchTasksResultVo res = batchTaskService.getBatchTasksResult(batchTaskId);
        return CommonResult.success(res, "获取结果成功");
    }

    @GetMapping("/history")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult getBatchTasksHistory(@RequestParam Integer page, @RequestParam Integer pageSize) {
        GetBatchTasksHistoryVo res = batchTaskService.getBatchTasksHistory(page, pageSize);
        return CommonResult.success(res, "获取历史成功");
    }

    @GetMapping("/myCodeList")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult getCodeByUser(@RequestParam Integer page, @RequestParam Integer pageSize) {
        GetCodeForAdminVo res = batchTaskService.getCodeByUser(page, pageSize);
        return CommonResult.success(res, "获取代码成功");
    }

    @GetMapping("/codes")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult getCodesByIds(@RequestParam List<Integer> id) {
        List<Code> res = batchTaskService.getCodesByIds(id);
        return CommonResult.success(res, "获取代码成功");
    }

}
