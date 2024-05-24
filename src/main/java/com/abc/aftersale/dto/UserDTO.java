package com.abc.aftersale.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 11:47
 * @注释 user类数据传输对象
 */
@Data
public class UserDTO implements Serializable {
    public Integer id;

    public String loginName;

    public String password;

    public Integer identity;

    public String michatId;

    public String name;

    public String phone;

    public String address;

    public Integer status;

    public Timestamp createTime;

    public Timestamp updateTime;

    public String token;
}
