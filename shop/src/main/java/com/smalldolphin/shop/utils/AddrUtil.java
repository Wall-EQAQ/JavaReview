package com.smalldolphin.shop.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldolphin.shop.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/20 21:09
 * @Modified by: 获取地址类
 */
public class AddrUtil {
    //打印日志用的
    private static final Logger log = LoggerFactory.getLogger(AddrUtil.class);

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        // 内网不查询
        if (IpUtil.internalIp(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = HttpUtil.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
            if (StringUtils.isEmpty(rspStr)) {
                log.error("获取地理位置异常 {}", ip);
                return UNKNOWN;
            }

            JSONObject obj = JSONObject.parseObject(rspStr);
            String region = obj.getString("pro");
            String city = obj.getString("city");
            return String.format("%s %s", region, city);
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", e);
        }

        return address;

    }
}
