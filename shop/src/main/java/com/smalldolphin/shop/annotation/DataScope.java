package com.smalldolphin.shop.annotation;

import java.lang.annotation.*;

/**
 * @Description:shop
 * @Created by Administrator on 2021/12/1 23:29
 * @Modified by:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    //部门表的别名
    String deptAlias() default "";
    //用户表的别名
    String userAlias() default "";
}
