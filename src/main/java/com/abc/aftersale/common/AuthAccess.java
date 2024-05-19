package com.abc.aftersale.common;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 15:39
 * @注释 在接口上添加该注解，以越过鉴权
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
}
