package com.abc.aftersale.common;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author zhaoranzhi
 * @create 2024-05-24-21:48
 */
@Component
public class BestPayConfig {

    @Autowired
    WxAccountConfig wxAccountConfig;

    @Autowired
    AlipayAccountConfig alipayAccountConfig;

//    TODO: 规范配置，把商户id等信息放在ymal里面
    @Bean
    public BestPayService bestPayConfigService(WxPayConfig wxPayConfig){


        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);

        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(alipayAccountConfig.getAppId());
        aliPayConfig.setPrivateKey(alipayAccountConfig.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(alipayAccountConfig.getPublicKey());

        // 使用内网穿透模拟外部ip，地址要加上/pay/notify
        aliPayConfig.setNotifyUrl("https://4957afuy9122.vicp.fun//pay/notify");
        aliPayConfig.setReturnUrl("http://127.0.0.1");

        bestPayService.setAliPayConfig(aliPayConfig);

        return bestPayService;

    }
    @Bean
    public WxPayConfig wxPayConfig(){
        WxPayConfig wxPayConfig = new WxPayConfig();
//        公众号账号
//        wxPayConfig.setAppId("wxd898fcb01713c658");
        wxPayConfig.setAppId(wxAccountConfig.getAppId());
        // 商户id
        wxPayConfig.setMchId(wxAccountConfig.getMchId());
        // 商户密钥
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());
        // 使用内网穿透模拟外部ip，地址要加上/pay/notify
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());

        return wxPayConfig;
    }
}
