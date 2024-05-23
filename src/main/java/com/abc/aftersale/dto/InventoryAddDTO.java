package com.abc.aftersale.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhaoranzhi
 * @create 2024-05-22-16:37
 */
@Data
public class InventoryAddDTO {

    private Integer id;

    private String inventoryName;

    private String inventoryClass;

    private Integer inventoryNumber;

    private BigDecimal inventoryPrice;

}
