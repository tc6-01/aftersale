package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.service.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Resource;

/**
 * 工单复检状态自动调用系统任务
 * 接收工程师上传自检视频，更新工单
 */
@Service
public class orderEnsureReturn implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取流程变量
        Order order = (Order) delegateExecution.getVariable("returnEdOrder");
        // 更新流程变量order
        delegateExecution.setVariable("order", order);
        System.out.println("===============当前处于用户确认工单返回状态，工单详情：" + order.toString());
    }
}
