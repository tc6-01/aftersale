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
    int updateOrderDTO(OrderDTO source);


    OrderDTO accept(OrderDTO orderDTO, Integer engineerId);

    OrderDTO maintenance(OrderDTO orderDTO, Integer engineerId, Boolean isFaulty, String desc);

    OrderDTO apply(OrderDTO orderDTO, Integer engineerId, Boolean isMaterial, InventoryDTO inventoryDTO);

    OrderDTO orderReturn(Integer orderId, Integer engineerId);

    OrderDTO confirmReceipt(OrderDTO orderDTO);

//    OrderDTO costPush(Integer orderId, Integer engineerId, Boolean isMaterial, InventoryDTO inventoryDTO);
}
