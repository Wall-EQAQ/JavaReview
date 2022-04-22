package com.smalldolphin.shop.utils;


import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @Description:shop
 * @Created by Administrator on 2021/4/12 22:15
 * @Modified by:
 */
public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;
    //状态码
    public static final String CODE = "code";
    //返回消息
    public static final String MSG = "msg";
    //返回数据
    public static final String DATA = "data";

    //状态类型
    public enum Type {

        SUCCESS(0),
        WARN(301),
        ERROR(500);

        private final int value;
        Type(int value){
            this.value = value;
        }
        public int value() {
           return this.value;
        }
    }
    //返回一个空消息
    public AjaxResult() {}

    public AjaxResult(Type type, String msg) {
        put(CODE,type.value);
        put(MSG,msg);
    }
    public AjaxResult(Type type, String msg, Object data) {
        put(CODE,type.value);
        put(MSG,msg);
        if (!StringUtil.isNull(data)) {
            put(DATA, data);
        }
    }
    //返回成功消息
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }
    //返回成功数据
    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功", data);
    }
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(Type.SUCCESS, msg, data);
    }

    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(Type.ERROR, msg, data);
    }
}
