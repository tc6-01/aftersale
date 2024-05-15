package com.abc.aftersale.exception;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/15 10:26
 * @注释
 */
public class ServiceException extends RuntimeException{
    public ServiceException(String msg) {
        super(msg);
    }
}
