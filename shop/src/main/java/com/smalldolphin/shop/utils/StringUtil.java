package com.smalldolphin.shop.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/17 18:50
 * @Modified by:
 */
public class StringUtil extends StringUtils {

    private static final String NULLSTR = "";
    private static final char SEPARATOR = '_';

    /**
     *  判断一个字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断一个对象是否为空
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }
    /**
     *  判断 Collection 是否为空
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     *  是否包含字符串
     * @param str   验证字符串
     * @param strings   字符串组
     * @return  包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strings) {
        if (str != null && strings != null) {
            for (String s : strings) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  下划线转驼峰命名
     *  user_name -->>  userName
     * @param str
     * @return
     */
    public static String toUnderScoreCase(String str) {
        if(str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean preCharIsUpperCase = true;
        boolean currCharIsUpperCase = true;
        boolean nextCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i-1));
            }else {
                preCharIsUpperCase = false;
            }

            currCharIsUpperCase = Character.isUpperCase(c);
            if (i < (str.length() - 1)) {
                nextCharIsUpperCase = Character.isUpperCase(str.charAt(i+1));
            }
            //  AAc   为啥要结尾追加一个 _   ???   这一段没看懂
            if (preCharIsUpperCase && currCharIsUpperCase && !nextCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && currCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
