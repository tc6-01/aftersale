package com.abc.aftersale.service.impl;

import com.abc.aftersale.common.Result;
import com.abc.aftersale.dto.OrderPayDTO;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.OrderMapper;
import com.abc.aftersale.service.PayService;
import com.abc.aftersale.utils.MichatIdGenerator;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author zhaoranzhi
 * @create 2024-05-24-15:27
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    BestPayService bestPayService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    MichatIdGenerator michatIdGenerator;

    // 支付状态：0-未创建；1-已创建；2-待支付；3-已支付；
    private final Integer NOT_CREATED = 0;

    private final Integer CREATED = 1;

    private final Integer WAIT_PAY = 2;

    private final Integer PAID = 3;

    /**
     * 创建支付
     * @param orderId 工单id，生成订单id，由于没有设计订单id算法，暂时由工单号+10位随机数代替
     * @param bestPayTypeEnum 支付方式：微信/支付宝
     * @return 返回支付二维码
     */
    @Override
    public PayResponse create(Integer orderId, BestPayTypeEnum bestPayTypeEnum) {
        // 写入数据库, 修改real_cost, 修改支付状态为待支付
        Order payOrder = orderMapper.selectById(orderId);
        if (!payOrder.getPayStatus().equals(CREATED)){
            throw new ServiceException("工程师未推送订单");
        }

        payOrder.setPayStatus(WAIT_PAY);

        // 生成订单id，由工单号+10位随机数得到
        String payOrderId = orderId + michatIdGenerator.generatorOrderId(10);
        payOrder.setPayId(payOrderId);

        // 由于接入的是第三方商户，这里暂且将费用都设为0.01元，避免向商户支付过高的费用
        BigDecimal amount = new BigDecimal(0.01);
        payOrder.setRealCost(amount);

        orderMapper.updateById(payOrder);

        PayRequest payRequest = new PayRequest();
        payRequest.setOrderName("6046229-工单支付");

        payRequest.setOrderId(payOrderId);
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(bestPayTypeEnum);

        PayResponse response = bestPayService.pay(payRequest);
        log.info("发起支付 response={}", response);

        return response;
    }

    /**
     * 异步处理
     * @param notifyData 微信/支付宝发回的异步通知
     * @return 返回给微信/支付宝信息，通知他们不再继续发异步通知
     */
    @Override
    public String asyncNotify(String notifyData) {
        // 签名校验
        PayResponse response = bestPayService.asyncNotify(notifyData);
        log.info("异步通知response={}",response);

        // 金额校验
        Order dbOrder = orderMapper.selectByPayId(response.getOrderId());
        if (dbOrder == null){
            throw new ServiceException("通过payOrderId查到的是null");
        }
        if (!dbOrder.getPayStatus().equals(PAID)){
            if (dbOrder.getRealCost().compareTo(BigDecimal.valueOf(response.getOrderAmount())) != 0){
                throw new ServiceException("异步通知中的金额和数据库中不一致！, pay_id为" + response.getOrderId());
            }
            // 修改订单支付状态
            dbOrder.setPayStatus(PAID);
            // dbOrder.setStatus(7);
            orderMapper.updateById(dbOrder);
        }


        if (response.getPayPlatformEnum() == BestPayPlatformEnum.WX){
            // 通知微信
            return "<xml>\n" +
                    "   <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "   <return_msg><![CDATA[OK]]></return_msg>" +
                    "</xml>";
        } else if (response.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            // 通知支付宝
            return "success";
        }
        throw new ServiceException("异步通知中错误的支付平台");

    }

    /**
     * 查询支付记录（通过工单号）
     * @param orderId 工单号
     * @return
     */
    @Override
    public OrderPayDTO queryByOrderId(Integer orderId) {
        Order dbOrder = orderMapper.selectById(orderId);
        OrderPayDTO payDTO = new OrderPayDTO();
        payDTO.setId(dbOrder.getId());
        payDTO.setUserId(dbOrder.getUserId());
        payDTO.setUserName(dbOrder.getUserName());
        payDTO.setUserPhone(dbOrder.getUserPhone());
        payDTO.setUserAddress(dbOrder.getUserAddress());
        payDTO.setProductInfo(dbOrder.getProductInfo());
        payDTO.setSnInfo(dbOrder.getSnInfo());
        payDTO.setPredCost(dbOrder.getPredCost());
        payDTO.setRealCost(dbOrder.getRealCost());
        payDTO.setPayStatus(dbOrder.getPayStatus());

        return payDTO;
    }
}
