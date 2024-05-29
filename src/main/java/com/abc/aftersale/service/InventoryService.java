package com.abc.aftersale.service;

import com.abc.aftersale.dto.InventoryDTO;
import com.abc.aftersale.entity.Inventory;

import java.util.List;

/**
 * @author zhaoranzhi
 * @create 2024-05-22-16:39
 */
public interface InventoryService {

    Inventory addInventory(Integer userId, InventoryDTO inventoryDTO);

    Inventory updateInventory(Integer userId, InventoryDTO inventoryDTO);

    List<InventoryDTO> list(Integer userId, InventoryDTO inventoryDTO);
}
