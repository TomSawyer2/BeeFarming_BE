package com.bf.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.modules.user.dto.LoginDto;
import com.bf.modules.user.dto.RegisterDto;
import com.bf.modules.user.model.User;

public interface UserService extends IService<User> {
    public Boolean register(RegisterDto registerDto);
    public String login(LoginDto loginDto);
}
