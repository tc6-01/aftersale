package com.abc.aftersale.exception;

import lombok.Getter;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/15 10:26
 * @注释
 */
@Getter
public class ServiceException extends RuntimeException{

    private String code;

    public ServiceException(String msg) {
        super(msg);
        this.code = "500";
    }

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
