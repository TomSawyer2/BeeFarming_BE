package com.bf.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.common.api.ResultCode;
import com.bf.common.exception.Asserts;
import com.bf.common.util.JwtUtils;
import com.bf.modules.user.dto.LoginDto;
import com.bf.modules.user.dto.RegisterDto;
import com.bf.modules.user.mapper.UserMapper;
import com.bf.modules.user.model.User;
import com.bf.modules.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Boolean register(RegisterDto registerDto) {
        return false;
    }

    @Override
    public String login(LoginDto loginDto) {
        return null;
    }
}
