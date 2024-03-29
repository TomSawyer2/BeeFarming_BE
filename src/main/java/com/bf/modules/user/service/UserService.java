package com.bf.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.modules.user.dto.LoginDto;
import com.bf.modules.user.dto.RegisterDto;
import com.bf.modules.user.model.User;
import com.bf.modules.user.vo.GetUserInfoVo;

public interface UserService extends IService<User> {
    public void register(RegisterDto registerDto);
    public String login(LoginDto loginDto);
    public GetUserInfoVo getUserInfo();
}
