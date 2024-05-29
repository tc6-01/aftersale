package com.abc.aftersale.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaoranzhi
 * @create 2024-05-27-15:17
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxAccountConfig {
    private String appId;

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String returnUrl;
}
