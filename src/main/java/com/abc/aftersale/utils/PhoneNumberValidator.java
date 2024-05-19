package com.abc.aftersale.utils;

import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/15 13:51
 * @注释
 */
@Component
public class PhoneNumberValidator {
    public static boolean validatePhoneNumber(String phoneNumber, int expectedLength) {
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", ""); // 去除非数字字符
        return digitsOnly.length() == expectedLength;
    }
}
