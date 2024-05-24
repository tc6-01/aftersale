package com.abc.aftersale.process.serviceTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.File;
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
public class orderPay implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取流程变量
        OrderDTO order = (OrderDTO) delegateExecution.getVariable("selfCheckOrder");
        String payId = (String) delegateExecution.getVariable("payId");
        // 更新order中的自检视频字段
        // 更新流程变量order
        delegateExecution.setVariable("order", order);
        System.out.println("当前处于工单已支付状态，工单详情：" + order.toString());
        System.out.println("支付ID ：" + payId);
        //TODO： 需要提供一个支付成功后根据payID检测是否支付的接口
//        入参：
//        {
//            "payId" : String, # 支付ID
//            "fee" : Integer # 支付费用
//        }
//        出参：
//        {
//            "status" : Integer  # 是否支付成功（支付ID与费用一致）
//        }
        // 更新工单
        orderService.create(order);
    }
}
