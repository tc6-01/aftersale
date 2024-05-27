package com.abc.aftersale.controller;

import com.abc.aftersale.common.AuthAccess;
import com.abc.aftersale.common.Result;
import com.abc.aftersale.dto.OrderPayDTO;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.service.PayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoranzhi
 * @create 2024-05-24-16:31
 */

@Slf4j
@Controller
@RequestMapping("/pay")
public class payController {

    @Autowired
    PayService payService;

    @Autowired
    private WxPayConfig wxPayConfig;

    /**
     * 创建支付
     */
    @AuthAccess
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") Integer orderId,
                               @RequestParam("payType")BestPayTypeEnum bestPayTypeEnum){

        PayResponse response = payService.create(orderId,bestPayTypeEnum);

        Map<String, String> map = new HashMap<>();

        // 支付方式不同，渲染不同
        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE){
            map.put("codeUrl", response.getCodeUrl());
            map.put("orderId", String.valueOf(orderId));
            map.put("returnUrl", wxPayConfig.getReturnUrl());
            return new ModelAndView("createForWxNative", map);
        }else if (bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC){
            map.put("body", response.getBody());
            return new ModelAndView("createForAlipayPC", map);
        }
        throw new ServiceException("暂不支持此支付方式！");
    }

    /**
     * 异步处理
     */
    @AuthAccess
    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData){
        return payService.asyncNotify(notifyData);
//        log.info("notifyData={}", notifyData);
    }

    /**
     * 查询支付记录（通过工单号）
     */
    @GetMapping("queryByOrderId")
    @ResponseBody
    public OrderPayDTO queryByOrderId(@RequestParam Integer orderId){
        return payService.queryByOrderId(orderId);
    }
}
