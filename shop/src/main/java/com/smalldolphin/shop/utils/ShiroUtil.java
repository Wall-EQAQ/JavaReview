package com.smalldolphin.shop.utils;

import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.shiro.AuthRealm;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/15 21:13
 * @Modified by:    Shiro工具类
 */
public class ShiroUtil {

    /**
     * 获取当前登录的Subject
     * @return
     */
    public static SysUser getUser() {
        SysUser user = null;
        Object obj = SecurityUtils.getSubject().getPrincipal();
        if (!StringUtil.isNull(obj)) {
            user = new SysUser();
            BeanUtil.copyBeanProp(user, obj);
        }
        return user;
    }

    public static void setUser(SysUser user) {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        String realmName = principals.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user,realmName);
        //重新加载 Principal
        subject.runAs(newPrincipalCollection);

    }

    public static String getSessionId() {
        return String.valueOf(SecurityUtils.getSubject().getSession().getId());
    }

    public static void clearCachedAuthorizationInfo() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        AuthRealm authRealm = (AuthRealm) rsm.getRealms().iterator().next();
        authRealm.clearCachedAuthorizationInfo();
    }

    //生成随机盐，此处生成的3字节，字符串长度为6
    public static String randomSalt() {
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }

}
