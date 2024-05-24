package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.dto.UserDTO;
import com.abc.aftersale.entity.File;
import com.abc.aftersale.service.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工程师接单
 * 关联工单与对应工程师，更新工单
 */
@Service
public class orderTake implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取流程变量
        OrderDTO order = (OrderDTO) delegateExecution.getVariable("orderTake");
        UserDTO engineer = (UserDTO) delegateExecution.getVariable("engineer");
        // 更新关联工程师
        order.setEngineerId(engineer.getId());
        order.setEngineerDesc(engineer.getName());
        delegateExecution.setVariable("order",order);
        // 更新工单（传入数据库）
        orderService.create(order);
    }
}
