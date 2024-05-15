package com.abc.aftersale.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/15 14:01
 * @注释 时间工具类
 */
@Component
public class DateUtil {
    public Timestamp getCurrentTimestamp() {
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentTime);
        return timestamp;
    }
}
