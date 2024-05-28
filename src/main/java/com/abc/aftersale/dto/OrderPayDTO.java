package com.abc.aftersale.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author zhaoranzhi
 * @create 2024-05-27-11:05
 */
@Data
public class OrderPayDTO extends CommonQueryDTO implements Serializable {
    public Integer id;

    public Integer userId;

    public String userName;

    public String userPhone;

    public String userAddress;

    public String productInfo;

    public String snInfo;

    public BigDecimal predCost;

    public BigDecimal realCost;

    public Integer payStatus;
}
