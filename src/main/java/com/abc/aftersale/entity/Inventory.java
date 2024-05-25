package com.abc.aftersale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("inventory")
public class Inventory implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String inventoryName;

    private String inventoryClass;

    private Integer inventoryNumber;

    private BigDecimal inventoryPrice;

    private Timestamp createTime;

    private Timestamp updateTime;
}
