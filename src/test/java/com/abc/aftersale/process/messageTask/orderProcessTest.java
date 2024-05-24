package com.abc.aftersale.process.messageTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.dto.UserDTO;
import com.abc.aftersale.entity.File;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class orderProcessTest {

    @Resource
    private RuntimeService runtimeService;
    @Test
    void processUserCreate() {
        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
    }

    @Test
    void processUserEnsure() {
        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
        // 流程ID
        String executionId = processInstance.getProcessInstanceId();
        // 订阅后续的消息
        List<byte[]> fileList = new ArrayList<>();
        for (int i = 0 ; i < 3; i++) {
            byte[] bytes = new byte[10];
            bytes[0] = '1';
            bytes[1] = '2';
            fileList.add(bytes);
        }
        paraMap.put("enSureOrder", order);
        paraMap.put("files", fileList);
        // 开启消息订阅，直接触发
        MessageCorrelationResult messageUserEnsure = runtimeService.createMessageCorrelation("Message_user_ensure")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageUserEnsure.getResultType());

    }

    @Test
    void processEngineerTake() {
        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
        // 流程ID
        String executionId = processInstance.getProcessInstanceId();
        /**
         * 用户确认工单
         */

        List<byte[]> fileList = new ArrayList<>();
        for (int i = 0 ; i < 3; i++) {
            byte[] bytes = new byte[10];
            bytes[0] = '1';
            bytes[1] = '2';
            fileList.add(bytes);
        }
        paraMap.put("enSureOrder", order);
        paraMap.put("files", fileList);
        // 开启消息订阅，直接触发
        MessageCorrelationResult messageUserEnsure = runtimeService.createMessageCorrelation("Message_user_ensure")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageUserEnsure.getResultType());
        /**
         * 工程师接收工单
         */
        paraMap.put("takingOrder", order);
        UserDTO engineer = new UserDTO();
        engineer.setId(01);
        engineer.setName("王洋洋");
        engineer.setIdentity(1);
        engineer.setPhone("14775498296");
        paraMap.put("engineer", engineer);
        MessageCorrelationResult messageEngineerTake = runtimeService.createMessageCorrelation("Message_engineer_take")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerTake.getResultType());
    }

    @Test
    void processEngineerCheck() {
        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
        // 流程ID
        String executionId = processInstance.getProcessInstanceId();
        /**
         * 用户确认工单
         */

        List<byte[]> fileList = new ArrayList<>();
        for (int i = 0 ; i < 3; i++) {
            byte[] bytes = new byte[10];
            bytes[0] = '1';
            bytes[1] = '2';
            fileList.add(bytes);
        }
        paraMap.put("enSureOrder", order);
        paraMap.put("files", fileList);
        // 开启消息订阅，直接触发
        MessageCorrelationResult messageUserEnsure = runtimeService.createMessageCorrelation("Message_user_ensure")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageUserEnsure.getResultType());
        /**
         * 工程师接收工单
         */
        paraMap.put("takingOrder", order);
        UserDTO engineer = new UserDTO();
        engineer.setId(01);
        engineer.setName("王洋洋");
        engineer.setIdentity(1);
        engineer.setPhone("14775498296");
        paraMap.put("engineer", engineer);
        MessageCorrelationResult messageEngineerTake = runtimeService.createMessageCorrelation("Message_engineer_take")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerTake.getResultType());
        /**
         * 工程师检测
         */
        Boolean needMainTain = new Boolean(true);
        paraMap.put("need_main_tain", needMainTain);
        paraMap.put("checkingOrder", order);
        MessageCorrelationResult messageEngineerCheck = runtimeService.createMessageCorrelation("Message_engineer_check")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerCheck.getResultType());
    }

    @Test
    void processEngineerMainTain() {
        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
        // 流程ID
        String executionId = processInstance.getProcessInstanceId();
        /**
         * 用户确认工单
         */

        List<byte[]> fileList = new ArrayList<>();
        for (int i = 0 ; i < 3; i++) {
            byte[] bytes = new byte[10];
            bytes[0] = '1';
            bytes[1] = '2';
            fileList.add(bytes);
        }
        paraMap.put("enSureOrder", order);
        paraMap.put("files", fileList);
        // 开启消息订阅，直接触发
        MessageCorrelationResult messageUserEnsure = runtimeService.createMessageCorrelation("Message_user_ensure")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageUserEnsure.getResultType());
        /**
         * 工程师接收工单
         */
        paraMap.put("takingOrder", order);
        UserDTO engineer = new UserDTO();
        engineer.setId(01);
        engineer.setName("王洋洋");
        engineer.setIdentity(1);
        engineer.setPhone("14775498296");
        paraMap.put("engineer", engineer);
        MessageCorrelationResult messageEngineerTake = runtimeService.createMessageCorrelation("Message_engineer_take")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerTake.getResultType());
        /**
         * 工程师检测
         */
        Boolean needMainTain = new Boolean(true);
        paraMap.put("need_main_tain", needMainTain);
        paraMap.put("checkingOrder", order);
        MessageCorrelationResult messageEngineerCheck = runtimeService.createMessageCorrelation("Message_engineer_check")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerCheck.getResultType());
        /**
         * 工程师维修
         */
        Boolean needMaterial = new Boolean(true);
        paraMap.put("need_material", needMaterial);
        paraMap.put("mainTainOrder", order);
        MessageCorrelationResult messageEngineerMaintain = runtimeService.createMessageCorrelation("Message_engineer_maintain")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerMaintain.getResultType());
    }

    @Test
    void processEngineerSelfCheck() {
        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create", paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
        // 流程ID
        String executionId = processInstance.getProcessInstanceId();
        /**
         * 用户确认工单
         */

        List<byte[]> fileList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            byte[] bytes = new byte[10];
            bytes[0] = '1';
            bytes[1] = '2';
            fileList.add(bytes);
        }
        paraMap.put("enSureOrder", order);
        paraMap.put("files", fileList);
        // 开启消息订阅，直接触发
        MessageCorrelationResult messageUserEnsure = runtimeService.createMessageCorrelation("Message_user_ensure")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageUserEnsure.getResultType());
        /**
         * 工程师接收工单
         */
        paraMap.put("takingOrder", order);
        UserDTO engineer = new UserDTO();
        engineer.setId(01);
        engineer.setName("王洋洋");
        engineer.setIdentity(1);
        engineer.setPhone("14775498296");
        paraMap.put("engineer", engineer);
        MessageCorrelationResult messageEngineerTake = runtimeService.createMessageCorrelation("Message_engineer_take")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerTake.getResultType());
        /**
         * 工程师检测
         */
        Boolean needMainTain = new Boolean(true);
        paraMap.put("need_main_tain", needMainTain);
        paraMap.put("checkingOrder", order);
        MessageCorrelationResult messageEngineerCheck = runtimeService.createMessageCorrelation("Message_engineer_check")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerCheck.getResultType());
        /**
         * 工程师维修
         */
        Boolean needMaterial = new Boolean(true);
        paraMap.put("need_material", needMaterial);
        paraMap.put("mainTainOrder", order);
        MessageCorrelationResult messageEngineerMaintain = runtimeService.createMessageCorrelation("Message_engineer_maintain")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerMaintain.getResultType());
        /**
         * 工程师提交复检视频
         */
        byte[] video = new byte[10];
        video[0] = '1';
        video[1] = '2';
        paraMap.put("video", video);
        paraMap.put("selfCheckOrder", order);
        MessageCorrelationResult messageEngineerSelfCheck = runtimeService.createMessageCorrelation("Message_engineer_selfCheck")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerSelfCheck.getResultType());

    }
    @Test
    void processEngineerPaySuccess() {
        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create", paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
        // 流程ID
        String executionId = processInstance.getProcessInstanceId();
        /**
         * 用户确认工单
         */

        List<byte[]> fileList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            byte[] bytes = new byte[10];
            bytes[0] = '1';
            bytes[1] = '2';
            fileList.add(bytes);
        }
        paraMap.put("enSureOrder", order);
        paraMap.put("files", fileList);
        // 开启消息订阅，直接触发
        MessageCorrelationResult messageUserEnsure = runtimeService.createMessageCorrelation("Message_user_ensure")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageUserEnsure.getResultType());
        /**
         * 工程师接收工单
         */
        paraMap.put("takingOrder", order);
        UserDTO engineer = new UserDTO();
        engineer.setId(01);
        engineer.setName("王洋洋");
        engineer.setIdentity(1);
        engineer.setPhone("14775498296");
        paraMap.put("engineer", engineer);
        MessageCorrelationResult messageEngineerTake = runtimeService.createMessageCorrelation("Message_engineer_take")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerTake.getResultType());
        /**
         * 工程师检测
         */
        Boolean needMainTain = new Boolean(true);
        paraMap.put("need_main_tain", needMainTain);
        paraMap.put("checkingOrder", order);
        MessageCorrelationResult messageEngineerCheck = runtimeService.createMessageCorrelation("Message_engineer_check")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerCheck.getResultType());
        /**
         * 工程师维修
         */
        Boolean needMaterial = new Boolean(true);
        paraMap.put("need_material", needMaterial);
        paraMap.put("mainTainOrder", order);
        MessageCorrelationResult messageEngineerMaintain = runtimeService.createMessageCorrelation("Message_engineer_maintain")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerMaintain.getResultType());
        /**
         * 工程师提交复检视频
         */
        byte[] video = new byte[10];
        video[0] = '1';
        video[1] = '2';
        paraMap.put("video", video);
        paraMap.put("selfCheckOrder", order);
        MessageCorrelationResult messageEngineerSelfCheck = runtimeService.createMessageCorrelation("Message_engineer_selfCheck")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerSelfCheck.getResultType());
        /**
         * 用户已支付成功
         */
        String payId = new String("payId167557");
        paraMap.put("payId", payId);
        paraMap.put("returnOrder", order);
        MessageCorrelationResult messagePaySuccess = runtimeService.createMessageCorrelation("Message_pay_success")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messagePaySuccess.getResultType());
    }

    @Test
    void processEngineerEnsureReturn() {

        OrderDTO order = new OrderDTO();
        order.setUserId(3);
        order.setId(100000000);
        order.setUserName("王洋洋");
        order.setUserPhone("14775498196");
        order.setProductInfo("小米14Pro-测试确认功能");
        order.setSnInfo("SN123");
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create", paraMap);
        System.out.println("==================流程ID" + processInstance.getProcessInstanceId());
        // 流程ID
        String executionId = processInstance.getProcessInstanceId();
        /**
         * 用户确认工单
         */

        List<byte[]> fileList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            byte[] bytes = new byte[10];
            bytes[0] = '1';
            bytes[1] = '2';
            fileList.add(bytes);
        }
        paraMap.put("enSureOrder", order);
        paraMap.put("files", fileList);
        // 开启消息订阅，直接触发
        MessageCorrelationResult messageUserEnsure = runtimeService.createMessageCorrelation("Message_user_ensure")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageUserEnsure.getResultType());
        /**
         * 工程师接收工单
         */
        paraMap.put("takingOrder", order);
        UserDTO engineer = new UserDTO();
        engineer.setId(01);
        engineer.setName("王洋洋");
        engineer.setIdentity(1);
        engineer.setPhone("14775498296");
        paraMap.put("engineer", engineer);
        MessageCorrelationResult messageEngineerTake = runtimeService.createMessageCorrelation("Message_engineer_take")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerTake.getResultType());
        /**
         * 工程师检测
         */
        Boolean needMainTain = new Boolean(true);
        paraMap.put("need_main_tain", needMainTain);
        paraMap.put("checkingOrder", order);
        MessageCorrelationResult messageEngineerCheck = runtimeService.createMessageCorrelation("Message_engineer_check")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerCheck.getResultType());
        /**
         * 工程师维修
         */
        Boolean needMaterial = new Boolean(true);
        paraMap.put("need_material", needMaterial);
        paraMap.put("mainTainOrder", order);
        MessageCorrelationResult messageEngineerMaintain = runtimeService.createMessageCorrelation("Message_engineer_maintain")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerMaintain.getResultType());
        /**
         * 工程师提交复检视频
         */
        byte[] video = new byte[10];
        video[0] = '1';
        video[1] = '2';
        paraMap.put("video", video);
        paraMap.put("selfCheckOrder", order);
        MessageCorrelationResult messageEngineerSelfCheck = runtimeService.createMessageCorrelation("Message_engineer_selfCheck")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEngineerSelfCheck.getResultType());
        /**
         * 用户已支付成功
         */
        String payId = new String("payId167557");
        paraMap.put("payId", payId);
        paraMap.put("returnOrder", order);
        MessageCorrelationResult messagePaySuccess = runtimeService.createMessageCorrelation("Message_pay_success")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messagePaySuccess.getResultType());
        /**
         * 用户确认收货
         */
        paraMap.put("returnEdOrder", order);
        MessageCorrelationResult messageEnsureReturn = runtimeService.createMessageCorrelation("Message_ensure_return")
                .processInstanceId(executionId)
                .setVariables(paraMap).correlateWithResult();
        System.out.println("==============成功触发消息事件====================" + messageEnsureReturn.getResultType());
        System.out.println("业务完成");
    }
}