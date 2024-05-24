package com.abc.aftersale.service;

import com.abc.aftersale.dto.InventoryDTO;
import com.abc.aftersale.dto.OrderDTO;

import java.util.List;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 20:00
 * @注释
 */
public interface OrderService {
    OrderDTO create(OrderDTO orderDTO);

    List<OrderDTO> list(OrderDTO orderDTO);

    OrderDTO cancel(OrderDTO orderDTO);

    OrderDTO processDetails(OrderDTO orderDTO);

    OrderDTO accept(Integer orderId, Integer engineerId);

    OrderDTO maintenance(Integer orderId, Integer engineerId, Boolean isFaulty, String desc);

    OrderDTO apply(Integer orderId, Integer engineerId, Boolean isMaterial, InventoryDTO inventoryDTO);
}
