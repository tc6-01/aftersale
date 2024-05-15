package com.abc.aftersale.controller;

import cn.hutool.core.util.StrUtil;
import com.abc.aftersale.common.Result;
import com.abc.aftersale.entity.User;
import com.abc.aftersale.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/14 23:39
 * @注释
 */
@SuppressWarnings({"all"})
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getLoginName()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("用户名或密码输入非法!");
        }
        user = userServiceImpl.loginService(user);
        return Result.success(user);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getLoginName()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("用户名或密码输入非法!");
        }
        user = userServiceImpl.registerService(user);
        return Result.success(user);
    }
}
