package com.abc.aftersale.service.impl;

import com.abc.aftersale.dto.InventoryAddDTO;
import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.OrderMapper;
import com.abc.aftersale.mapper.UserMapper;
import com.abc.aftersale.service.EngineerService;
import com.abc.aftersale.service.InventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhaoranzhi
 * @create 2024-05-20-11:19
 */
@Service
public class EngineerServiceImpl implements EngineerService {

    final Integer USER_CLAIM = 2; //用户确认

    final Integer CLAIM = 3; //工程师确认

    final Integer MAINTENANCE = 4; //维修

    final Integer REINSPECTION = 5; //复检

    final Integer RETURNUSER = 7; //复检

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    InventoryService inventoryService;

    @Override
    public OrderDTO update(Integer orderId, Integer engineerId) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);

        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }


        if (dbOrder.getStatus().equals(USER_CLAIM)){
            // 修改工单状态，添加工程师信息
            dbOrder.setStatus(CLAIM);
            dbOrder.setEngineerId(engineerId);

            orderMapper.updateByPrimaryKeySelective(dbOrder);
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(dbOrder, orderDTO);

            return orderDTO;
        }else{
            throw new ServiceException("当前工单前置工作未确认，无法修改状态。");
        }


    }

    /*
    人工检修
    状态变更："工程师已接单--3" ----> "设备维修中--4"
            "工程师已接单--3" ----> "返还待确认--7"
     */
    @Override
    public OrderDTO maintenance(Integer orderId, Integer engineerId, Boolean isFaulty, String desc) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(CLAIM)){
            if (isFaulty){
                // 修改工单状态为“设备维修中”，添加维修信息
                dbOrder.setStatus(MAINTENANCE);

                dbOrder.setEngineerId(engineerId);
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }else {
                // 返还待确认
                dbOrder.setStatus(RETURNUSER);

                dbOrder.setEngineerId(engineerId);
                // 没有进行维修，需要返还理由
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }
        }else{
            throw new ServiceException("当前工单工程师未认领，无法修改状态。");
        }

    }

    /*
    申请物料
    状态变更：'设备维修中--4"  ----> "人工复检中--5"
    申请物料暂时由工程师直接进行物料表的增减
     */
    @Override
    public OrderDTO apply(Integer orderId, Integer engineerId, Boolean isMaterial, InventoryAddDTO inventoryAddDTO) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(MAINTENANCE)){
            if (isMaterial){
                // 申请物料
                Integer num = -inventoryAddDTO.getInventoryNumber();
                inventoryAddDTO.setInventoryNumber(num);
                inventoryService.addInventory(engineerId, inventoryAddDTO);

                // 修改状态为人工复检中
                dbOrder.setStatus(REINSPECTION);
                dbOrder.setEngineerId(engineerId);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;

            }else{
                // 修改状态为人工复检中
                dbOrder.setStatus(REINSPECTION);
                dbOrder.setEngineerId(engineerId);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }
        }else{
            throw new ServiceException("当前工单未进行维修确认，申请物料失败。");
        }
    }
}
