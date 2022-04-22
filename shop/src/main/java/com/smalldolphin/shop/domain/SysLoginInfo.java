package com.smalldolphin.shop.domain;

import java.util.Date;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/20 20:21
 * @Modified by:  系统访问记录表 sys_logininfor
 */
public class SysLoginInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    //登录账号
    private String loginName;
    //登录状态  0成功   1失败
    private String status;
    //登录IP
    private String ipaddr;
    //登录地址
    private String location;
    //浏览器类型
    private String browser;
    //操作系统
    private String os;
    //提示消息
    private String msg;
    //登录时间
    private Date loginTime;

    public SysLoginInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "SysLoginInfo{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", status='" + status + '\'' +
                ", ipaddr='" + ipaddr + '\'' +
                ", location='" + location + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", msg='" + msg + '\'' +
                ", loginTime=" + loginTime +
                '}';
    }
}
