package com.abc.aftersale.service;

import com.abc.aftersale.dto.EngineerDTO;
import com.abc.aftersale.dto.InventoryAddDTO;
import com.abc.aftersale.dto.OrderDTO;

/**
 * @author zhaoranzhi
 * @create 2024-05-20-11:05
 * 工程师认领工单，人工检修，
 */
public interface EngineerService {

    OrderDTO update(Integer orderId, Integer engineerId);

    OrderDTO maintenance(Integer orderId, Integer engineerId, Boolean isFaulty, String desc);

    OrderDTO apply(Integer orderId, Integer engineerId, Boolean isMaterial, InventoryAddDTO inventoryAddDTO);
}
