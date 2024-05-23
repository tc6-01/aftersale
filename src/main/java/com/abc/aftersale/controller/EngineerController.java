package com.abc.aftersale.controller;

import com.abc.aftersale.common.Result;
import com.abc.aftersale.dto.EngineerDTO;
import com.abc.aftersale.dto.InventoryAddDTO;
import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.service.EngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

/**
 * @author zhaoranzhi
 * @create 2024-05-20-11:17
 */
@RestController
@RequestMapping("/engineer")
public class EngineerController {

    @Autowired
    EngineerService engineerService;

    /*
    工程师修改工单状态
    工单状态变更："用户已确认--2" ----> "工程师已接单--3"
 */
    @PutMapping("/update")
    public Result update(@RequestParam("orderId") Integer orderId,
                         @RequestParam("orderStatus") Integer orderStatus,
                         @RequestParam("engineerId") Integer engineerId){
        OrderDTO orderDTO = engineerService.update(orderId, orderStatus, engineerId);
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
                              @RequestParam("desc") String desc){
        OrderDTO orderDTO = engineerService.maintenance(orderId, engineerId, isFaulty, desc);
        return Result.success(orderDTO);
    }

    /**
    物料申请：工程师针对有故障的设备，根据检测情况决定是否需要物料申请
    工单状态变更：'设备维修中--4"  ----> "人工复检中--5"
     */
    /**
     *
     * @param orderId
     * @param engineerId
     * @param isMaterial
     * @param inventoryAddDTO
     * @return
     */
    @PutMapping("/materialApplication")
    public Result materialApplication(@RequestParam("orderId") Integer orderId,
                                      @RequestParam("engineerId") Integer engineerId,
                                      @RequestParam("isFaulty") Boolean isMaterial,
                                      @RequestBody InventoryAddDTO inventoryAddDTO){
        OrderDTO orderDTO = engineerService.apply(orderId, engineerId, isMaterial, inventoryAddDTO);
        return Result.success(orderDTO);
    }

}
