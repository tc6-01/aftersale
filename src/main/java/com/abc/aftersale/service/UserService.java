package com.abc.aftersale.service;

import com.abc.aftersale.dto.UserDTO;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/14 23:32
 * @注释
 */
public interface UserService {
    public UserDTO loginService(UserDTO userDTO);

    public UserDTO registerService(UserDTO userDTO);
}
