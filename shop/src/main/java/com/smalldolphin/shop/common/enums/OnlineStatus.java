package com.smalldolphin.shop.common.enums;

/**
 * @Description:    用户在线状态
 * @Created by Administrator on 2021/7/15 22:46
 * @Modified by:
 */
public enum OnlineStatus {

    on_line("在线"), off_line("离线");

    private final String info;

    private OnlineStatus(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
