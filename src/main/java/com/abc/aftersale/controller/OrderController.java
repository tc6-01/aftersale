package com.abc.aftersale.controller;

import com.abc.aftersale.common.Result;
import com.abc.aftersale.dto.InventoryDTO;
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
    public Result confirm(@RequestParam("orderId") Integer orderId, @RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            throw new ServiceException("上传文件为空！");
        }
        List<File> dbFiles = fileService.upload(files, orderId);
        return Result.success(dbFiles);
    }

    @PostMapping("/processDetails")
    public Result processDetails(@RequestBody OrderDTO orderDTO) {
        orderDTO = orderService.processDetails(orderDTO);
        return Result.success(orderDTO);
    }

    /**
     工程师修改工单状态
     工单状态变更："用户已确认--2" ----> "工程师已接单--3"
     */
    @PutMapping("/accept")
    public Result accept(@RequestParam("orderId") Integer orderId,
                         @RequestParam("engineerId") Integer engineerId){
        OrderDTO orderDTO = orderService.accept(orderId, engineerId);
        return Result.success(orderDTO);
    }

    /**
     5、人工检修：工程师进行人工检测并添加故障检测结果
     5.1 有故障：工程师进行设备维修
     工单状态变更："工程师已接单--3" ----> "设备维修中--4"
     5.2 无故障：等待工程师进行人工复检
     工单状态变更："工程师已接单--3" ----> "返还待确认--7"
     */
    @PutMapping("/maintenance")
    public Result maintenance(@RequestParam("orderId") Integer orderId,
                              @RequestParam("engineerId") Integer engineerId,
                              @RequestParam("isFaulty") Boolean isFaulty,
                              @RequestParam("engineerDesc") String engineerDesc){
        OrderDTO orderDTO = orderService.maintenance(orderId, engineerId, isFaulty, engineerDesc);
        return Result.success(orderDTO);
    }

    /**
     物料申请：工程师针对有故障的设备，根据检测情况决定是否需要物料申请
     工单状态变更：'设备维修中--4"  ----> "人工复检中--5"
     */
    @PutMapping("/inventoryApplication")
    public Result materialApplication(@RequestParam("orderId") Integer orderId,
                                      @RequestParam("engineerId") Integer engineerId,
                                      @RequestParam("isInventory") Boolean isInventory,
                                      @RequestBody InventoryDTO inventoryDTO){
        OrderDTO orderDTO = orderService.apply(orderId, engineerId, isInventory, inventoryDTO);
        return Result.success(orderDTO);
    }

    @PostMapping("/recheck")
    public Result recheck(@RequestParam("orderId") Integer orderId, @RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            throw new ServiceException("上传文件为空！");
        }
        List<File> dbFiles = fileService.upload(files, orderId);
        return Result.success(dbFiles);
    }

    @PutMapping("/return")
    public Result orderReturn(@RequestParam("orderId") Integer orderId,
                         @RequestParam("engineerId") Integer engineerId){
        OrderDTO orderDTO = orderService.orderReturn(orderId, engineerId);
        return Result.success(orderDTO);
    }

    @PostMapping("/confirmReceipt")
    public Result confirmReceipt(@RequestBody OrderDTO orderDTO) {
        orderDTO = orderService.confirmReceipt(orderDTO);
        return Result.success(orderDTO);
    }
}
