package com.abc.aftersale.service.impl;


import com.abc.aftersale.AftersaleApplicationTests;
import com.abc.aftersale.service.PayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author zhaoranzhi
 * @create 2024-05-24-15:56
 */
public class PayServiceImplTest extends AftersaleApplicationTests {

    @Autowired
    private PayService payService;

    @Test
    public void create() {
//        payService.create(3, BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
        payService.create(3, BestPayTypeEnum.WXPAY_NATIVE);
    }
}