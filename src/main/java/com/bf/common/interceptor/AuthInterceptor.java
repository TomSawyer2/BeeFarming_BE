package com.bf.common.interceptor;

import com.bf.common.annotation.LoginRequired;
import com.bf.common.api.CommonResult;
import com.bf.common.api.ResultCode;
import com.bf.common.enums.Permission;
import com.bf.common.exception.Asserts;
import com.bf.common.util.JwtUtils;
import com.bf.modules.user.mapper.UserMapper;
import com.bf.modules.user.model.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;


    public AuthInterceptor(JwtUtils jwtUtils, UserMapper userMapper) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(LoginRequired.class)) {
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            int userId = jwtUtils.getUserIdFromToken(request);
            if (userId != -1) {
                // 权限校验
                return auth(loginRequired.needPermission(), userId);
            } else {
                Asserts.fail(ResultCode.TOKEN_MISSING);
            }
        }
        return true;
    }

    /**
     * 释放ThreadLocal中的数据
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @param ex       异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        currentUser.remove();
    }

    private boolean auth(Permission[] permissions, Integer id) {
        User user = userMapper.selectById(id);
        for (Permission p : permissions) {
            if (user != null){
                currentUser.set(user);
                return true;
            }
            else break;
        }
        return false;
    }

}
