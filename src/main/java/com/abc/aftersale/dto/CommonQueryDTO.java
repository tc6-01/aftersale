package com.abc.aftersale.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/17 11:08
 * @注释 公共查询条件
 */
@Data
public class CommonQueryDTO implements Serializable {
    // 当前分页
    public Long pageNum;

    // 分页大小
    public Long pageSize;

    public Long totalNum;
}
