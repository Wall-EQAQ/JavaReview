package com.smalldolphin.shop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/19 21:37
 * @Modified by: 处理并记录日志文件
 * 返回形式  [username][jsessionid][ip][accept][UserAgent][url][params][Referer]
 */
public class LogUtil {

    public static final Logger ERROR_LOG = LoggerFactory.getLogger("sys-error");

    public static final Logger ACCESS_LOG = LoggerFactory.getLogger("sys-access");

    //拼装返回格式
    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }


}
