package com.abc.aftersale.controller;

import com.abc.aftersale.common.Result;

import com.abc.aftersale.dto.InventoryAddDTO;
import com.abc.aftersale.entity.Inventory;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhaoranzhi
 * @create 2024-05-22-16:28
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    //修改库存，包括修改物料名称，类型，数量，价格
    @PostMapping("/updateInventory")
    public Result updateInventory(@RequestParam("userId") Integer userId,
                                  @RequestBody InventoryAddDTO inventoryAddDTO){
        Inventory inventory = inventoryService.updateInventory(userId, inventoryAddDTO);
        return Result.success(inventory);
    }

    //添加库存
    @PostMapping("/addInventory")
    public Result addInventory(@RequestParam("userId") Integer userId,
                               @RequestBody InventoryAddDTO inventoryAddDTO){
        Inventory inventory = inventoryService.addInventory(userId, inventoryAddDTO);
        return Result.success(inventory);
    }

}
