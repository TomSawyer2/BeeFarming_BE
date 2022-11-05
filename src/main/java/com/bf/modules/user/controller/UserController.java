package com.bf.modules.user.controller;

import com.bf.common.api.CommonResult;
import com.bf.common.api.ResultCode;
import com.bf.modules.user.dto.LoginDto;
import com.bf.modules.user.dto.RegisterDto;
import com.bf.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public CommonResult register(@RequestBody RegisterDto registerDto) {
        Boolean res = userService.register(registerDto);
        if (res) return CommonResult.success();
        else return CommonResult.failed(ResultCode.USER_EXISTS);
    }

    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginDto loginDto) {
        return CommonResult.success(userService.login(loginDto), "登录成功");
    }
}