package com.abc.aftersale.controller;

import com.abc.aftersale.common.Result;

import com.abc.aftersale.dto.InventoryDTO;
import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.Inventory;
import com.abc.aftersale.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhaoranzhi
 * @create 2024-05-22-16:28
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    //修改库存，包括修改在库物料的数量，价格
    @PostMapping("/updateInventory")
    public Result updateInventory(@RequestParam("userId") Integer userId,
                                  @RequestBody InventoryDTO inventoryDTO){
        Inventory inventory = inventoryService.updateInventory(userId, inventoryDTO);
        return Result.success(inventory);
    }

    //新增物料（之前不在库中的物料）
    @PostMapping("/addInventory")
    public Result addInventory(@RequestParam("userId") Integer userId,
                               @RequestBody InventoryDTO inventoryDTO){
        Inventory inventory = inventoryService.addInventory(userId, inventoryDTO);
        return Result.success(inventory);
    }

    @PostMapping("/list")
    public Result list(@RequestParam("userId") Integer userId,
                       @RequestBody InventoryDTO inventoryDTO) {
        List<InventoryDTO> inventoryDTOList = inventoryService.list(userId, inventoryDTO);
        return Result.success(inventoryDTOList);
    }

}
