package com.bf.modules.batchTasks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.common.api.ResultCode;
import com.bf.common.exception.Asserts;
import com.bf.common.interceptor.AuthInterceptor;
import com.bf.common.util.JwtUtils;
import com.bf.modules.batchTasks.dto.UploadCodeForBatchTasksDto;
import com.bf.modules.batchTasks.mapper.BatchTaskMapper;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.service.BatchTaskService;
import com.bf.modules.code.mapper.CodeMapper;
import com.bf.modules.code.model.Code;
import com.bf.modules.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchTaskServiceImpl extends ServiceImpl<BatchTaskMapper, BatchTask> implements BatchTaskService {

    @Autowired
    BatchTaskMapper batchTaskMapper;

    @Autowired
    CodeMapper codeMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void uploadCode(UploadCodeForBatchTasksDto uploadCodeForBatchTasksDto) {
        // 如果有codeId，说明是更新代码
        if (uploadCodeForBatchTasksDto.getCodeId() != null) {
            Code code = codeMapper.selectById(uploadCodeForBatchTasksDto.getCodeId());
            if (code == null) {
                Asserts.fail(ResultCode.CODE_NOT_EXIST);
            }
            code.setContent(uploadCodeForBatchTasksDto.getContent());
            codeMapper.updateById(code);
        } else {
            // 如果没有codeId，说明是上传代码
            Code code = new Code();
            code.setContent(uploadCodeForBatchTasksDto.getContent());
            // 获取当前用户
            User currentUser = AuthInterceptor.getCurrentUser();
            Integer currentUserId = currentUser.getId();
            code.setUserId(currentUserId);
            codeMapper.insert(code);
        }
    }
}
