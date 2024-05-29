package com.abc.aftersale.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhaoranzhi
 * @create 2024-05-22-16:37
 */
@Data
public class InventoryDTO extends CommonQueryDTO {

    private Integer id;

    private String inventoryName;

    private Integer inventoryClass;

    private Integer inventoryNumber;

    private BigDecimal inventoryPrice;

    private String inventoryClassName;

}
