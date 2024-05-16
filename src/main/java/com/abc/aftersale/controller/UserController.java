package com.abc.aftersale.controller;

import cn.hutool.core.util.StrUtil;
import com.abc.aftersale.common.AuthAccess;
import com.abc.aftersale.common.Result;
import com.abc.aftersale.dto.UserDTO;
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

    @AuthAccess
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        if (StrUtil.isBlank(userDTO.getLoginName()) || StrUtil.isBlank(userDTO.getPassword())) {
            return Result.error("用户名或密码输入非法!");
        }
        userDTO = userServiceImpl.loginService(userDTO);
        return Result.success(userDTO);
    }

    @AuthAccess
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        if (StrUtil.isBlank(userDTO.getLoginName()) || StrUtil.isBlank(userDTO.getPassword())) {
            return Result.error("用户名或密码输入非法!");
        }
        userDTO = userServiceImpl.registerService(userDTO);
        return Result.success(userDTO);
    }
}
