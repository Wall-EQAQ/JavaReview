package com.smalldolphin.shop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @Description:shop
 * @Created by Administrator on 2021/3/5 18:41
 * @Modified by:
 */

@DisplayName("junit5功能测试类")
public class TestJunit5 {

    @DisplayName("测试DisplayName注解")
    @Test
    void testDisplayName(){
        System.out.println("1");
    }

    @BeforeEach
    void testBeforeEach(){
        System.out.println("测试方法就要开始了");
    }

    @AfterEach
    void testAfterEach(){
        System.out.println("测试方法结束了");
    }


}
