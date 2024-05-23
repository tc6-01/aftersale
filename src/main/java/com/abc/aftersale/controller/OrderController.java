package com.abc.aftersale.controller;

import com.abc.aftersale.common.Result;
import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.File;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.service.impl.FileServiceImpl;
import com.abc.aftersale.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 19:48
 * @注释 订单模块功能接口
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    FileServiceImpl fileService;

    @PostMapping("/create")
    public Result create(@RequestBody OrderDTO orderDTO) {
        orderDTO = orderService.create(orderDTO);
        return Result.success(orderDTO);
    }

    @PostMapping("/list")
    public Result list(@RequestBody OrderDTO orderDTO) {
        List<OrderDTO> orderDTOList = orderService.list(orderDTO);
        return Result.success(orderDTOList);
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestBody OrderDTO orderDTO) {
        orderDTO = orderService.cancel(orderDTO);
        return Result.success(orderDTO);
    }

    @PostMapping("/confirm")
    public Result confirm(@RequestParam("orderId") Integer orderId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException("上传文件为空！");
        }
        File dbFile = fileService.upload(file, orderId);
        return Result.success(dbFile);
    }

    @PostMapping("/processDetails")
    public Result processDetails(@RequestBody OrderDTO orderDTO) {
        orderDTO = orderService.processDetails(orderDTO);
        return Result.success(orderDTO);
    }


}
