package com.abc.aftersale.process.messageTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.dto.UserDTO;
import com.abc.aftersale.entity.File;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@EnableProcessApplication
public class orderProcess {
    @Resource
    private RuntimeService runtimeService;

    /**
     * 触发消息任务---Message_user_create
     * @param order 用户输入订单信息
     * @desc 用户输入相关工单详情后，通过消息任务开启一个新的流程实例
     */
    public String processUserCreate(OrderDTO order){
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println(processInstance.toString());
        return processInstance.getProcessInstanceId();
    }
    /**
     * 触发消息任务---Message_user_ensure
     * @param orderDTO 用户确认工单时工单对象
     * @param pictures 用户上传故障图片
     * @desc 用户确认工单时，上传图片后，创建中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processUserEnsure(List<File> pictures, OrderDTO orderDTO, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("enSureOrder", orderDTO);
        paraMap.put("files", pictures);
        // 第一个是Message reference 第二个是Message Event Id,用于绑定
        runtimeService.messageEventReceived("Message_user_ensure",executionId, paraMap);
    }
    /**
     * 触发消息任务---Message_engineer_take
     * @param engineer 工程师对象
     * @param order 工程师接受的工单
     * @desc 在用户提交工单的工单基础上进一步获取更为详细的工单（上传相应工程师），中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerTake(UserDTO engineer, OrderDTO order, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("engineer", engineer);
        paraMap.put("takingOrder", order);
        // 第一个是Message reference 第二个是Message Event Id
        runtimeService.messageEventReceived("Message_engineer_take",executionId, paraMap);
    }
    /**
     * 触发消息任务---Message_engineer_check
     * @param needMainTain 是否需要维修
     * @param order 工程师检测的工单
     * @desc 获取工程师检测工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerCheck(Boolean needMainTain, OrderDTO order, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("need_main_tain", needMainTain);
        paraMap.put("checkingOrder", order);
        // 第一个是Message reference 第二个是Message Event Id
        runtimeService.messageEventReceived("Message_engineer_check",executionId, paraMap);
    }
    /**
     * 触发消息任务---Message_engineer_maintain
     * @param needMaterial 是否需要物料
     * @param order 工程师维修的工单
     * @desc 获取工程师维修工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerMainTain(Boolean needMaterial, OrderDTO order, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("need_material", needMaterial);
        paraMap.put("mainTainOrder", order);
        // 第一个是Message reference 第二个是Message Event Id
        runtimeService.messageEventReceived("Message_engineer_maintain",executionId, paraMap);
    }
    /**
     * 触发消息任务---Message_engineer_selfCheck
     * @param video 上传自检视频
     * @param order 工程师自检的工单
     * @desc 获取工程师自检工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerSelfCheck(File video, OrderDTO order, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("video", video);
        paraMap.put("selfCheckOrder", order);
        // 第一个是Message reference 第二个是Message Event Id
        runtimeService.messageEventReceived("Message_engineer_selfCheck",executionId, paraMap);
    }
    /**
     * 触发消息任务---Message_pay_success
     * @param payId 支付流水ID
     * @param order 处于用户待支付的工单
     * @desc 获取支付工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerPaySuccess(OrderDTO order, String payId, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("payId", payId);
        paraMap.put("returnOrder", order);
        // 第一个是Message reference 第二个是Message Event Id
        runtimeService.messageEventReceived("Message_pay_success",executionId, paraMap);
    }
    /**
     * 触发消息任务---Message_ensure_return
     * @param order 确认收货的工单
     * @desc 获取收货后的工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerEnsureReturn(OrderDTO order, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("returnOrder", order);
        // 第一个是Message reference 第二个是Message Event Id
        runtimeService.messageEventReceived("Message_ensure_return",executionId, paraMap);
    }

}