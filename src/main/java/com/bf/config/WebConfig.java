package com.bf.config;

import com.bf.common.interceptor.AuthInterceptor;
import com.bf.common.util.JwtUtils;
import com.bf.modules.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserMapper userMapper;

    /**
     * 添加拦截器
     *
     * @param registry 注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtUtils, userMapper)).addPathPatterns("/**");
    }

}
