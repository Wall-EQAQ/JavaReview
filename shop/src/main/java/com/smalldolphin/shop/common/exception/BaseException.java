package com.smalldolphin.shop.common.exception;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * @Description:    基础异常
 * @Created by Administrator on 2021/5/10 21:19
 * @Modified by:
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    //所属模块
    private String module;
    //错误码
    private String code;
    //错误码对应参数
    private Object[] args;
    //错误消息
    private String defaultMessage;

    public BaseException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }
    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }


}
