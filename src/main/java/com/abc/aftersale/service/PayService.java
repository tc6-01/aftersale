package com.abc.aftersale.service;

import com.abc.aftersale.common.Result;
import com.abc.aftersale.dto.OrderPayDTO;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * @author zhaoranzhi
 * @create 2024-05-24-15:24
 */
public interface PayService {

    /**
     * 创建支付
     * @param orderId
     * @return
     */
    PayResponse create(Integer orderId, BestPayTypeEnum bestPayTypeEnum);

    /**
     * 异步通知处理
     * @param notifyData
     */
    String asyncNotify(String notifyData);

    /**
     * 查询支付记录（通过工单号）
     * @param orderId
     * @return
     */
    OrderPayDTO queryByOrderId(Integer orderId);

    OrderPayDTO reset(Integer orderId);
}
