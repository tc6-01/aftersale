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
 * @Date 2024/5/17 18:20
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file")
public class File {
    @TableId(type = IdType.AUTO)
    public Integer id;

    public Integer orderId;

    public String fileName;

    public byte[] fileData;

    public Integer fileType;

    public Timestamp createTime;

    public Timestamp updateTime;
}
