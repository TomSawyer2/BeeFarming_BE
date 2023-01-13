package com.bf.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.modules.admin.service.AdminService;
import com.bf.modules.admin.vo.GetCodeForAdminVo;
import com.bf.modules.admin.vo.GetUserInfoForAdminVo;
import com.bf.modules.batchTasks.mapper.BatchTaskMapper;
import com.bf.modules.batchTasks.model.BatchTask;
import com.bf.modules.batchTasks.vo.GetBatchTasksHistoryVo;
import com.bf.modules.code.mapper.CodeMapper;
import com.bf.modules.code.model.Code;
import com.bf.modules.user.mapper.UserMapper;
import com.bf.modules.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl extends ServiceImpl<BatchTaskMapper, BatchTask> implements AdminService {

    @Autowired
    BatchTaskMapper batchTaskMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CodeMapper codeMapper;

    @Override
    public GetBatchTasksHistoryVo getBatchTasksHistoryForAdmin(int page, int pageSize) {
        Page<BatchTask> batchTaskPage = new Page<>(page, pageSize);
        QueryWrapper<BatchTask> queryWrapper = new QueryWrapper<>();
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
    public GetUserInfoForAdminVo getUserInfoForAdmin(int page, int pageSize) {
        Page<User> userPage = new Page<>(page, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        userMapper.selectPage(userPage, queryWrapper);
        List<User> users = userPage.getRecords();
        GetUserInfoForAdminVo res = new GetUserInfoForAdminVo();
        res.setPage(page);
        res.setPageSize(pageSize);
        res.setTotal((int) userPage.getTotal());
        res.setUserList(users);
        return res;
    }

    @Override
    public GetCodeForAdminVo getCodeForAdmin(int page, int pageSize) {
        Page<Code> codePage = new Page<>(page, pageSize);
        QueryWrapper<Code> queryWrapper = new QueryWrapper<>();
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
}
