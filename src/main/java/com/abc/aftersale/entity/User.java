package com.abc.aftersale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/14 21:10
 * @注释 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
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
}
