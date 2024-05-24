package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.service.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Resource;

/**
 * 工单维修状态自动调用系统任务
 * 更新工单中的详细维修信息
 */
@Service
public class orderMaintain implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取用户创建工单输入
        OrderDTO order = (OrderDTO) delegateExecution.getVariable("mainTainOrder");
        Boolean needMaterial = (Boolean) delegateExecution.getVariable("need_material");
        System.out.println("当前处于工单维修状态，工单详情：" + order.toString());
        System.out.println("当前工单是否需要申请物料: " + needMaterial);
        // 更新流程变量order
        delegateExecution.setVariable("order", order);
        // 执行相关校验工作……更新数据库表
    }
}
