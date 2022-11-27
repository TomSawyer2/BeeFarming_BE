package com.bf.modules.batchTasks.controller;

import com.bf.common.annotation.LoginRequired;
import com.bf.common.api.CommonResult;
import com.bf.common.enums.Permission;
import com.bf.modules.batchTasks.dto.RunBatchTasksDto;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.service.BatchTaskService;
import com.bf.modules.batchTasks.vo.GetBatchTasksStatusVo;
import com.bf.modules.batchTasks.vo.RunBatchTasksVo;
import com.bf.modules.batchTasks.vo.StopBatchTaskVo;
import com.bf.modules.batchTasks.vo.UploadCodeForBatchTasksVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        RunBatchTasksVo res = batchTaskService.runBatchTasks(runBatchTasksDto);
        return CommonResult.success(res, "开始运行");
    }

    @GetMapping("/status")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult getBatchTasksStatus(@RequestParam Integer batchTaskId) {
        GetBatchTasksStatusVo res = batchTaskService.getBatchTasksStatus(batchTaskId);
        return CommonResult.success(res, "状态查询成功");
    }

    @PutMapping("/stop")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult stopBatchTask(@RequestParam Integer batchTaskId) {
        StopBatchTaskVo res = batchTaskService.stopBatchTask(batchTaskId);
        return CommonResult.success(res, "停止成功");
    }

    @GetMapping("/testDocker")
    @LoginRequired(needPermission = Permission.USER)
    public CommonResult testDocker() {
        String res = batchTaskService.testDocker();
        return CommonResult.success(res, "获取Docker环境成功");
    }
}
