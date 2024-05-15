package com.abc.aftersale.exception;

import com.abc.aftersale.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/15 10:20
 * @注释 全局异常捕获
 */
@ControllerAdvice
@ResponseBody
public class GlobalException {

    @ExceptionHandler(ServiceException.class)
    public Result serviceException(ServiceException e) {
        return Result.error("500", e.getMessage());
    }
}
