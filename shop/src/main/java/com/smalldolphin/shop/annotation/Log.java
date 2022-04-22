package com.smalldolphin.shop.annotation;

import com.smalldolphin.shop.common.enums.BusinessType;
import com.smalldolphin.shop.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * @Description:    自定义操作日志记录注解
 *
 * @Retention(RetentionPolicy.RUNTIME)  表明该注解不仅被保存到class文件中，jvm加载class文件之后，任然存在
 * @Documented 注解表明这个注解应该被 javadoc工具记录
 *
 * @Created by Administrator on 2021/9/6 22:55
 * @Modified by:
 */
@Target({ElementType.TYPE_PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    //模块
    String title() default "";
    //功能
    BusinessType businessType() default BusinessType.OTHER;
    //操作人类别
    OperatorType operatorType() default OperatorType.MANAGE;
    //是否保存请求的参数
    boolean isSaveRequestData() default true;

}
