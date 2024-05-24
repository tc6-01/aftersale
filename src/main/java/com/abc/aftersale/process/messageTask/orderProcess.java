package com.abc.aftersale.process.messageTask;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.dto.UserDTO;
import com.abc.aftersale.entity.File;
import com.abc.aftersale.process.exception.processException;
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
     * @return 流程ID
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
        try{
            runtimeService.createMessageCorrelation("Message_user_ensure")
                    .processInstanceId(executionId)
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_user_ensure流程消息发送异常");
        }
    }
    /**
     * 触发消息任务---Message_engineer_take
     * @param engineer 工程师对象
     * @param order 工程师接受的工单
     * @desc 在用户提交工单的工单基础上进一步获取更为详细的工单（上传相应工程师），中间消息任务，通过发送消息直接触发（工程师检测）
     */
    public void processEngineerTake(UserDTO engineer, OrderDTO order, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("engineer", engineer);
        paraMap.put("takingOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_engineer_take")
                    .processInstanceId(executionId)
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_engineer_take流程消息发送异常");
        }
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
        try{
            runtimeService.createMessageCorrelation("Message_engineer_check")
                    .processInstanceId(executionId)
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_engineer_check流程消息发送异常");
        }
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
        try{
            runtimeService.createMessageCorrelation("Message_engineer_maintain")
                    .processInstanceId(executionId)
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_engineer_maintain流程消息发送异常");
        }
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
        try{
            runtimeService.createMessageCorrelation("Message_engineer_selfCheck")
                    .processInstanceId(executionId)
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_engineer_selfCheck流程消息发送异常");
        }
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
        try{
            runtimeService.createMessageCorrelation("Message_pay_success")
                    .processInstanceId(executionId)
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_pay_success流程消息发送异常");
        }
    }
    /**
     * 触发消息任务---Message_ensure_return
     * @param order 确认收货的工单
     * @desc 获取收货后的工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerEnsureReturn(OrderDTO order, String executionId){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("returnEdOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_ensure_return")
                    .processInstanceId(executionId)
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_ensure_return流程消息发送异常");
        }
    }

}
