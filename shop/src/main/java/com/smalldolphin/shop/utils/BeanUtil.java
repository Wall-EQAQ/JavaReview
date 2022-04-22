package com.smalldolphin.shop.utils;

import org.springframework.beans.BeanUtils;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/17 19:24
 * @Modified by:
 */
public class BeanUtil extends BeanUtils {

    /**
     * Bean属性复制工具方法
     * @param dest
     * @param src
     */
    public static void copyBeanProp(Object dest, Object src) {
        try {
            copyProperties(src, dest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
