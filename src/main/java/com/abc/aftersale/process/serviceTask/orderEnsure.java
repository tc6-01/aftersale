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
 * 接收用户上传文件，创建工单
 */
@Service
public class orderEnsure implements JavaDelegate {

    @Resource
    private OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取流程变量
        OrderDTO order = (OrderDTO) delegateExecution.getVariable("order");
        List<byte[]> fileList = (List<byte[]>) delegateExecution.getVariable("files");
        order.setImageFileList(fileList);
        delegateExecution.setVariable("order",order);
        System.out.println("当前处于工单确认状态，工单详情：" + order.toString());
        // 更新工单
//        orderService.create(order);
    }
}
