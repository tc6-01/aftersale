package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.File;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Resource;

/**
 * 工单复检状态自动调用系统任务
 * 接收工程师上传自检视频，更新工单
 */
@Slf4j
@Service
public class orderSelfCheck implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取流程变量
        Order order = (Order) delegateExecution.getVariable("selfCheckOrder");
        delegateExecution.setVariable("order", order);
        log.info("===================当前处于工单自检状态，工单详情：" + order.toString());
    }
}
