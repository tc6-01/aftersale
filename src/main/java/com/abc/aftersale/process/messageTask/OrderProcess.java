package com.abc.aftersale.process.messageTask;

import com.abc.aftersale.entity.Order;
import com.abc.aftersale.mapper.OrderMapper;
import com.abc.aftersale.process.exception.processException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
@Service
public class OrderProcess {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private OrderMapper orderMapper;

    /**
     * 触发消息任务---Message_user_create
     * @param order 用户输入订单信息
     * @return 流程ID
     * @desc 用户输入相关工单详情后，通过消息任务开启一个新的流程实例
     */
    public String processUserCreate(Order order){
        HashMap<String, Object> paraMap = new HashMap<>();

        paraMap.put("order", order);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("Message_user_create",paraMap);
        System.out.println(processInstance.toString());
        order.setInstanceId(processInstance.getProcessInstanceId());
        orderMapper.updateById(order);
        return processInstance.getProcessInstanceId();
    }
    /**
     * 触发消息任务---Message_user_ensure
     * @param orderId 用户确认工单时工单对象
     * @param pictures 用户上传故障图片
     * @desc 用户确认工单时，上传图片后，创建中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processUserEnsure(List<byte[]> pictures, Integer orderId){
        // 查询order
        Order order = orderMapper.selectById(orderId);
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("enSureOrder", order);
        paraMap.put("files", pictures);
        try{
            runtimeService.createMessageCorrelation("Message_user_ensure")
                    .processInstanceId(order.getInstanceId())
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_user_ensure流程消息发送异常");
        }
    }
    /**
     * 触发消息任务---Message_engineer_take
     * @param order 工程师接受的工单
     * @desc 在用户提交工单的工单基础上进一步获取更为详细的工单（上传相应工程师），中间消息任务，通过发送消息直接触发（工程师检测）
     */
    public void processEngineerTake(Order order){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("engineer", order.getEngineerId());
        paraMap.put("takingOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_engineer_take")
                    .processInstanceId(order.getInstanceId())
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
    public void processEngineerCheck(Boolean needMainTain, Order order){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("need_main_tain", needMainTain);
        paraMap.put("checkingOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_engineer_check")
                    .processInstanceId(order.getInstanceId())
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
    public void processEngineerMainTain(Boolean needMaterial, Order order){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("need_material", needMaterial);
        paraMap.put("mainTainOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_engineer_maintain")
                    .processInstanceId(order.getInstanceId())
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_engineer_maintain流程消息发送异常");
        }
    }
    /**
     * 触发消息任务---Message_engineer_selfCheck
     * @param video 上传自检视频
     * @param orderId 工程师自检的工单ID
     * @desc 获取工程师自检工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerSelfCheck(List<byte[]> video, Integer orderId){
        Order order = orderMapper.selectById(orderId);
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("video", video);
        paraMap.put("selfCheckOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_engineer_selfCheck")
                    .processInstanceId(order.getInstanceId())
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_engineer_selfCheck流程消息发送异常");
        }
    }
    /**
     * 触发消息任务---Message_pay_success
     * @param order 处于用户待支付的工单
     * @desc 获取支付工单，中间消息任务，通过发送消息直接触发（用户确认工单）
     */
    public void processEngineerPaySuccess(Order order){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("payId", order.getPayId());
        paraMap.put("returnOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_pay_success")
                    .processInstanceId(order.getInstanceId())
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
    public void processEngineerEnsureReturn(Order order){
        // 创建用于发送消息传递参数的Map
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("returnEdOrder", order);
        try{
            runtimeService.createMessageCorrelation("Message_ensure_return")
                    .processInstanceId(order.getInstanceId())
                    .setVariables(paraMap).correlateWithResult();
        }catch (Exception e) {
            throw new processException("Message_ensure_return流程消息发送异常");
        }
    }

}
