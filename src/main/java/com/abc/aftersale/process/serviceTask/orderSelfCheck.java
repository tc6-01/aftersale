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
public class orderSelfCheck implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取流程变量
        OrderDTO order = (OrderDTO) delegateExecution.getVariable("selfCheckOrder");
        byte[] video = (byte[]) delegateExecution.getVariable("video");
        // 更新order中的自检视频字段
        //TODO： 需要提供一个视频上传后与工单ID相关联的接口
//        入参：
//        {
//            "File" : Byte[], # 自检视频
//            "order" : order # 当前工单
//        }
//        出参：
//        {
//            "status" : Integer  # 是否上传成功
//        }
        // 更新工单
        // 更新流程变量order
        delegateExecution.setVariable("order", order);
        System.out.println("当前处于工单自检状态，工单详情：" + order.toString());
        System.out.println("自建视频上传 ：" + video);
    }
}
