package com.smalldolphin.shop.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/8 19:21
 * @Modified by:
 */

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.smalldolphin.shop.mapper")
public class ApplicationConfig {
}
