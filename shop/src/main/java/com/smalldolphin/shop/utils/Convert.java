package com.smalldolphin.shop.utils;

import java.math.BigDecimal;

/**
 * @Description:    类型转换器
 * @Created by Administrator on 2021/8/22 22:58
 * @Modified by:
 */
public class Convert {


    /**
     *  如果给定的值为null，或者转换失败，返回默认值 转换失败不会报错
     * @param value
     * @param defaultValue
     * @return
     */
    public static String toStr(Object value, String defaultValue) {
       if (value == null) {
           return defaultValue;
       }
       if (value instanceof String) {
           return (String) value;
       }
       return value.toString();

    }
    //转换为字符串
    public static String toStr(Object value) {
        return toStr(value, null);
    }

    /**
     *  转换为字符数组
     * @param str
     * @return
     */
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }

    public static String[] toStrArray(String split, String str) {
    return str.split(split);
    }




    /**
     * @param value 被转换的值
     * @param defaultValue  转化失败的默认值
     * @return
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            //小数转化为int类型时不会报错(报异常)
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try{
            return Integer.parseInt(valueStr.trim());
        }catch (Exception e) {
            return defaultValue;
        }

    }

    public static Integer toInt(Object value) {
        return toInt(value, null);
    }

    /**
     *  转换为Long,如果给定的值为空，或者转换失败，返回默认值
     * @param value 被转换的值
     * @param defaultValue
     * @return
     */
    public static Long toLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(valueStr.trim()).longValue();
        }catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     *  转换为Long数组
     * @param split 分隔符
     * @param str   被转换的值
     * @return
     */
    public static Long[] toLongArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Long[] {};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Long v = toLong(arr[i], null);
            longs[i] = v;
        }
        return longs;
    }
    //转换为long数组
    public static Long[] toLongArray(String str) {
        return toLongArray(",", str);
    }


}
