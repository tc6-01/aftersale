package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Resource;

/**
 * 工单申请状态自动调用系统任务
 * 获取用户工单输入，(不直接插入，等待后续用户确认后插入）
 */
@Service
public class orderApply implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取用户创建工单输入
        OrderDTO order = (OrderDTO) delegateExecution.getVariable("order");
        System.out.println("========================当前处于工单待创建状态，工单详情：" + order.toString());
        // 创建流程变量方便后续进行流程查询
        delegateExecution.setVariable("userId", order.getUserId());
        delegateExecution.setVariable("SN", order.getSnInfo());
        // 执行相关校验工作……
    }
}
