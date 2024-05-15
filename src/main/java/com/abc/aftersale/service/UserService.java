package com.abc.aftersale.service;

import com.abc.aftersale.entity.User;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/14 23:32
 * @注释
 */
public interface UserService {
    public User loginService(User user);

    public User registerService(User user);
}
