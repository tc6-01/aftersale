package com.abc.aftersale.process.service;

import com.abc.aftersale.dto.OrderDTO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class orderProcessTest {
    @Resource
    private RuntimeService  runtimeService;
    @Test
    void processUserCreate() {
        OrderDTO order = new OrderDTO();
        /**
         * "id": 3,
         *         "userId": 13,
         *         "userName": "小吴1",
         *         "userPhone": "13871723995",
         *         "userAddress": "武汉市",
         *         "productInfo": "小米14Pro-测试确认功能",
         *         "snInfo": "SN123",
         *         "status": 2,
         *         "userDesc": "屏幕异色",
         *         "invoiceInfo": "发票号123",
         *         "engineerId": null,
         *         "engineerDesc": null,
         *         "predCost": null,
         *         "realCost": null,
         *         "createTime": 1715933955000,
         *         "updateTime": 1715933955000,
         *         "imageFileList": [] //图片文件列表，此处省略
         */
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println("==================流程ID" + processInstance.toString());
    }
    @Test
    void processUserEnsure() {
    }

    @Test
    void processEngineerTake() {
    }
}