package com.bf.common.annotation;

import com.bf.common.enums.Permission;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {
    Permission[] needPermission();
}
