package com.bf.modules.batchTasks.controller;

import com.bf.common.annotation.LoginRequired;
import com.bf.common.api.CommonResult;
import com.bf.common.enums.Permission;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.service.BatchTaskService;
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
        batchTaskService.uploadCode(uploadCodeForBatchTasksDto);
        return CommonResult.success(null, "代码上传/更新成功");
    }
}
