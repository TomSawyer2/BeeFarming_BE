package com.bf.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.modules.admin.dto.BanUserDto;
import com.bf.modules.admin.vo.GetCodeForAdminVo;
import com.bf.modules.admin.vo.GetUserInfoForAdminVo;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.vo.GetBatchTasksHistoryVo;

public interface AdminService extends IService<BatchTask> {
    public GetBatchTasksHistoryVo getBatchTasksHistoryForAdmin(int page, int pageSize);
    public GetUserInfoForAdminVo getUserInfoForAdmin(int page, int pageSize);
    public GetCodeForAdminVo getCodeForAdmin(int page, int pageSize);
    public void banUser(BanUserDto banUserDto);
    public void unbanUser(BanUserDto banUserDto);
}
