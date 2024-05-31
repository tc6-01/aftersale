package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Resource;

/**
 * 工单物料申请状态自动调用系统任务
 * 调用相关接口实现物料申请
 */
@Slf4j
@Service
public class orderMaterialApply implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取用户创建工单输入
        Order order = (Order) delegateExecution.getVariable("order");
        // 物料申请接口 ， 需要相关返回值

        log.info("当前处于物料申请状态，工单详情：" + order.toString());
        log.info("物料申请情况: 正在等待中" );
//        更新订单中的物料申请情况
        // 更新流程变量order
        delegateExecution.setVariable("order", order);
        // 执行相关校验工作……
    }
}
