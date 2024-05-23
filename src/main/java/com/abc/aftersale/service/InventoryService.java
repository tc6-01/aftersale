package com.abc.aftersale.service;

import com.abc.aftersale.dto.InventoryAddDTO;
import com.abc.aftersale.entity.Inventory;
import com.abc.aftersale.entity.Order;

/**
 * @author zhaoranzhi
 * @create 2024-05-22-16:39
 */
public interface InventoryService {

    Inventory addInventory(Integer userId, InventoryAddDTO inventoryDTO);

    Inventory updateInventory(Integer userId, InventoryAddDTO inventoryDTO);

}
