package com.abc.aftersale.service.impl;

import com.abc.aftersale.dto.InventoryDTO;
import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.Inventory;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.entity.User;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.InventoryMapper;
import com.abc.aftersale.mapper.UserMapper;
import com.abc.aftersale.service.InventoryService;
import com.abc.aftersale.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    static Map<Integer, String> inventoryClassMap = new HashMap<>();
    static {
        inventoryClassMap.put(0, "小米手机");
        inventoryClassMap.put(1, "小米笔记本电脑");
        inventoryClassMap.put(2, "小米平板");
        inventoryClassMap.put(3, "小米电视");
        inventoryClassMap.put(4, "小米空调");
        inventoryClassMap.put(5, "小米SU7");
    }

    @Autowired
    InventoryMapper inventoryMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    DateUtil dateUtil;

    /**
     * 新增物料（当前库中不存在的物料）
     * 权限范围：库管，工程师
     * @param userId
     * @param inventoryDTO
     * @return
     */
    @Override
    public Inventory addInventory(Integer userId, InventoryDTO inventoryDTO) {

        // 身份校验
        User user = userMapper.selectById(userId);
        if (!user.getIdentity().equals(INVENTORY_ADMINISTRATOR)){
            throw new ServiceException("没有修改权限！");
        }

        Inventory inventory = new Inventory();
        String inventoryClass = inventoryClassMap.get(inventoryDTO.getInventoryClass());
        Inventory dbInventory = inventoryMapper.findByInventoryNameAndClass(inventoryDTO.getInventoryName(), inventoryClass);
        if (dbInventory != null) {
            throw new ServiceException("该物料已在库中！");
        } else {
            inventory.setInventoryName(inventoryDTO.getInventoryName());
            inventory.setInventoryClass(inventoryClass);
            inventory.setInventoryNumber(inventoryDTO.getInventoryNumber());
            inventory.setInventoryPrice(inventoryDTO.getInventoryPrice());
            inventory.setCreateTime(dateUtil.getCurrentTimestamp());
            inventory.setUpdateTime(dateUtil.getCurrentTimestamp());
            inventoryMapper.insert(inventory);
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
            throw new ServiceException("当前物料不存在！");
        }

        // 检验InventoryNumber
        if (inventoryDTO.getInventoryNumber() >= 0 && inventoryDTO.getInventoryNumber() <= MAX_INVENTORY){
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

    @Override
    public List<InventoryDTO> list(Integer userId, InventoryDTO inventoryDTO) {
        if (Objects.isNull(inventoryDTO.getPageNum()) || Objects.isNull(inventoryDTO.getPageSize())) {
            throw new ServiceException("请传入当前分页以及分页大小！");
        }
        Page<Inventory> page = new Page<>(inventoryDTO.getPageNum(), inventoryDTO.getPageSize());
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        // 判断用户身份
        User dbUser = userMapper.selectById(userId);
        if (dbUser == null) {
            throw new ServiceException("当前用户不存在！");
        }
        queryWrapper.eq(inventoryDTO.getInventoryClass() != null, "inventory_class", inventoryClassMap.get(inventoryDTO.getInventoryClass()));
        Page<Inventory> inventoryPage = inventoryMapper.selectPage(page, queryWrapper);
        List<InventoryDTO> inventoryDTOList = new ArrayList<>();
        Long totalNum = Long.valueOf(inventoryMapper.selectList(queryWrapper).size());
        for (Inventory inventory: inventoryPage.getRecords()) {
            InventoryDTO result = new InventoryDTO();
            result.setId(inventory.getId());
            result.setInventoryName(inventory.getInventoryName());
            result.setInventoryClassName(inventory.getInventoryClass());
            result.setInventoryNumber(inventory.getInventoryNumber());
            result.setInventoryPrice(inventory.getInventoryPrice());
            result.setPageNum(inventoryDTO.getPageNum());
            result.setPageSize(inventoryDTO.getPageSize());
            result.setTotalNum(totalNum);
            inventoryDTOList.add(result);
        }
        return inventoryDTOList;
    }
}
