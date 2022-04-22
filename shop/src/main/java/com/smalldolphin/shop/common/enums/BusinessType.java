package com.smalldolphin.shop.common.enums;

/**
 * @Description:    业务操作类型
 * @Created by Administrator on 2021/9/7 0:07
 * @Modified by:
 */
public enum BusinessType {
    INSERT,
    UPDATE,
    DELETE,
    GRANT,
    IMPORT,
    EXPORT,
    /**
     * 强退
     */
    FORCE,
    /**
     * 清空
     */
    CLEAN,
    /**
     * 其他
     */
    OTHER,
}
