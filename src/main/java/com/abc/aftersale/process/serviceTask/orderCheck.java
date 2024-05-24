package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.File;
import com.abc.aftersale.service.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工单待确认状态自动调用系统任务
 * 获取工程师提交检测内容，进行更新工单
 */
@Service
public class orderCheck implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取流程变量
        OrderDTO order = (OrderDTO) delegateExecution.getVariable("checkingOrder");
        Boolean needMainTain = (Boolean) delegateExecution.getVariable("need_main_tain");
        // 更新流程变量order
        delegateExecution.setVariable("order", order);
        System.out.println("=======================当前处于工单自检状态，工单详情：" + order.toString());
        System.out.println("=======================当前工单是否需要维修 ：" + needMainTain);

        // 进行维修
        // 更新工单
//        orderService.update(order);
    }
}
