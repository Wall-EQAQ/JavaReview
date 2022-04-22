package com.smalldolphin.shop.shiro.session;

import com.smalldolphin.shop.common.enums.OnlineStatus;
import org.apache.shiro.session.mgt.SimpleSession;

/**
 * @Description:    在线用户会话属性
 * @Created by Administrator on 2021/7/22 17:48
 * @Modified by:
 */
public class OnlineSession extends SimpleSession {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String loginName;

    /** 部门名称 */
    private String deptName;

    /** 用户头像 */
    private String avatar;

    /** 登录IP地址 */
    private String host;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;
    /** 用户在线状态 */
    private OnlineStatus status = OnlineStatus.on_line;
    /** 属性是否改变 优化session数据同步 */
    private transient boolean attributeChanged = false;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
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

    public OnlineStatus getStatus() {
        return status;
    }

    public void setStatus(OnlineStatus status) {
        this.status = status;
    }

    public boolean isAttributeChanged() {
        return attributeChanged;
    }

    public void setAttributeChanged(boolean attributeChanged) {
        this.attributeChanged = attributeChanged;
    }

    public void markAttributeChanged() {
        this.attributeChanged = true;
    }

    public void resetAttributeChanged() {
        this.attributeChanged = false;
    }

    //为什么要重写这2个方法呢
    @Override
    public void setAttribute(Object key, Object value) {
        super.setAttribute(key, value);
    }

    @Override
    public Object removeAttribute(Object key) {
        return super.removeAttribute(key);
    }
}
