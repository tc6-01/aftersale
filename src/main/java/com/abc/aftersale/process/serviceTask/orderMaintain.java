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
 * 工单维修状态自动调用系统任务
 * 更新工单中的详细维修信息
 */
@Slf4j
@Service
public class orderMaintain implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取用户创建工单输入
        Order order = (Order) delegateExecution.getVariable("mainTainOrder");
        Boolean needMaterial = (Boolean) delegateExecution.getVariable("need_material");
        log.info("=====================当前处于工单维修状态，工单详情：" + order.toString());
        log.info("当前工单是否需要申请物料: " + needMaterial);
        // 执行相关校验工作……更新数据库表
    }
}
