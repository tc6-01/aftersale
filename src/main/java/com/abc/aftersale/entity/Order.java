package com.abc.aftersale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 19:42
 * @注释 工单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    public Integer id;

    public Integer userId;

    public String userName;

    public String userPhone;

    public String userAddress;

    public String productInfo;

    public String snInfo;

    public Integer status;

    public String userDesc;

    public String invoiceInfo;

    public Integer engineerId;

    public String engineerDesc;

    public BigDecimal predCost;

    public BigDecimal realCost;

    public Integer payStatus;

    public String payId;

    public Timestamp createTime;

    public Timestamp updateTime;
}
