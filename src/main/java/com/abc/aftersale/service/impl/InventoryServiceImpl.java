package com.abc.aftersale.service.impl;

import com.abc.aftersale.dto.InventoryDTO;
import com.abc.aftersale.entity.Inventory;
import com.abc.aftersale.entity.User;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.InventoryMapper;
import com.abc.aftersale.mapper.UserMapper;
import com.abc.aftersale.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhaoranzhi
 * @create 2024-05-22-16:39
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    // 库存最大值
    final Integer MAX_INVENTORY = 9999;

    // 库管Identity
    final Integer INVENTORY_ADMINISTRATOR = 2;

    final Integer ENGINEER = 1;

    @Autowired
    InventoryMapper inventoryMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * 增加/减少物料
 *     权限范围：库管，工程师
     * @param userId
     * @param inventoryDTO
     * @return
     */

    @Override
    public Inventory addInventory(Integer userId, InventoryDTO inventoryDTO) {

        // 身份校验
        User user = userMapper.selectById(userId);
        if (!user.getIdentity().equals(INVENTORY_ADMINISTRATOR) && !user.getIdentity().equals(ENGINEER)){
            throw new ServiceException("没有修改权限！");
        }

        Inventory inventory = inventoryMapper.selectByPrimaryKey(inventoryDTO.getId());
        if (inventory == null){
            throw new ServiceException("当前物料不存在！");
        }

        // 物料校验
        if (!inventoryDTO.getInventoryName().equals(inventory.getInventoryName())){
            throw new ServiceException("物料名称不匹配，添加失败！");
        } else if (!inventoryDTO.getInventoryClass().equals(inventory.getInventoryClass())) {
            throw new ServiceException("物料类型不匹配，添加失败！！");
        } else if (!inventoryDTO.getInventoryPrice().equals(inventory.getInventoryPrice())) {
            throw new ServiceException("物料价格不匹配，添加失败！！");
        }else {

            Integer num = inventoryDTO.getInventoryNumber() + inventory.getInventoryNumber();

            inventory.setInventoryNumber(num);

            inventoryMapper.updateByPrimaryKeySelective(inventory);

            return inventory;
        }

    }

    /**
     * 修改库存信息，包括物料名称，物料类别，数量，价格
     * 权限范围：库管
     * @param userId
     * @param inventoryDTO
     * @return
     */
    @Override
    public Inventory updateInventory(Integer userId, InventoryDTO inventoryDTO) {

        // 身份校验
        User user = userMapper.selectById(userId);
        if (!user.getIdentity().equals(INVENTORY_ADMINISTRATOR)){
            throw new ServiceException("没有修改权限！");
        }

        Inventory inventory = inventoryMapper.selectByPrimaryKey(inventoryDTO.getId());
        if (inventory == null){
//            throw new ServiceException("当前物料不存在！");
            Inventory newInventory = new Inventory();
            newInventory.setInventoryName(inventoryDTO.getInventoryName());
            newInventory.setInventoryClass(inventoryDTO.getInventoryClass());
            newInventory.setInventoryNumber(inventoryDTO.getInventoryNumber());
            newInventory.setInventoryPrice(inventoryDTO.getInventoryPrice());

            inventoryMapper.insertSelective(newInventory);

            return newInventory;

        }

        // 检验InventoryNumber
        if (inventoryDTO.getInventoryNumber() >= 0 && inventoryDTO.getInventoryNumber() <= MAX_INVENTORY){
            inventory.setInventoryName(inventoryDTO.getInventoryName());
            inventory.setInventoryClass(inventoryDTO.getInventoryClass());
            inventory.setInventoryNumber(inventoryDTO.getInventoryNumber());
            inventory.setInventoryPrice(inventoryDTO.getInventoryPrice());

            Integer res = inventoryMapper.updateByPrimaryKeySelective(inventory);

            if (res.equals(0)){
                throw new ServiceException("物料添加失败！");
            }

            return inventory;
        }else {
            throw new ServiceException("物料数量不在指定范围内！");
        }
    }
}
