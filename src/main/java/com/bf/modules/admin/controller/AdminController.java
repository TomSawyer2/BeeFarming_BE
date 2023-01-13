package com.bf.modules.admin.controller;

import com.bf.common.annotation.LoginRequired;
import com.bf.common.api.CommonResult;
import com.bf.common.enums.Permission;
import com.bf.modules.admin.service.AdminService;
import com.bf.modules.admin.vo.GetCodeForAdminVo;
import com.bf.modules.admin.vo.GetUserInfoForAdminVo;
import com.bf.modules.batchTasks.vo.GetBatchTasksHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/batchTasks/history")
    @LoginRequired(needPermission = Permission.ADMIN)
    public CommonResult getBatchTasksHistoryForAdmin(@RequestParam Integer page, @RequestParam Integer pageSize) {
        GetBatchTasksHistoryVo res = adminService.getBatchTasksHistoryForAdmin(page, pageSize);
        return CommonResult.success(res, "获取历史成功");
    }

    @GetMapping("/userInfo")
    @LoginRequired(needPermission = Permission.ADMIN)
    public CommonResult getUserInfoForAdmin(@RequestParam Integer page, @RequestParam Integer pageSize) {
        GetUserInfoForAdminVo res = adminService.getUserInfoForAdmin(page, pageSize);
        return CommonResult.success(res, "获取所有用户信息成功");
    }

    @GetMapping("/code")
    @LoginRequired(needPermission = Permission.ADMIN)
    public CommonResult getCodeForAdmin(@RequestParam Integer page, @RequestParam Integer pageSize) {
        GetCodeForAdminVo res = adminService.getCodeForAdmin(page, pageSize);
        return CommonResult.success(res, "获取所有代码成功");
    }

}
