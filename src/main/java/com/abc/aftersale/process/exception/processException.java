package com.abc.aftersale.process.exception;

import com.abc.aftersale.exception.GlobalException;
import com.abc.aftersale.exception.ServiceException;

public class processException extends ServiceException {

    public processException(String msg) {
        super(msg);
    }

    public processException(String code, String msg) {
        super(code, msg);
    }
}
