package com.bf.modules.user.controller;

import com.bf.common.api.CommonResult;
import com.bf.common.api.ResultCode;
import com.bf.modules.user.dto.LoginDto;
import com.bf.modules.user.dto.RegisterDto;
import com.bf.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public CommonResult helloWorld() {
        return CommonResult.success(null, "Hello World for BF");
    }

    @PostMapping("/register")
    public CommonResult register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return CommonResult.success(null, "注册成功");
    }

    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginDto loginDto) {
        return CommonResult.success(userService.login(loginDto), "登录成功");
    }
}