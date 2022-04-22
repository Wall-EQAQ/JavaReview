package com.smalldolphin.shop.utils;

import com.smalldolphin.shop.common.exception.BaseException;
import org.apache.ibatis.jdbc.SQL;

/**
 * @Description:    Sql操作工具类
 * @Created by Administrator on 2021/8/23 23:22
 * @Modified by:
 */
public class SqlUtil {

    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";
    //验证 order by 语法是否符合规范
    public static String isValidOrderBySql(String value) {
        if (StringUtil.isNotEmpty(value) && !value.matches(SQL_PATTERN)) {
            throw new BaseException("参数不符合规范， 不能进行查询");
        }
        return value;
    }

}
