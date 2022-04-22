package com.smalldolphin.shop.shiro;

import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.service.MenuService;
import com.smalldolphin.shop.shiro.service.SysLoginService;
import com.smalldolphin.shop.utils.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(SysUser.class);

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private MenuService menuService;


    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser user = ShiroUtil.getUser();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //角色列表
        Set<String> roles = new HashSet<>();
        //菜单功能列表
        Set<String> menus = new HashSet<>();
        if (user.isAdmin()) {
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        }else {
            menus = menuService.selectPermsByUserId(user.getUserId());
            info.setStringPermissions(menus);
        }
        return info;
    }

    /**
     * 登录验证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String  password = new String(upToken.getPassword());
        SysUser user = null;
        try {
            user = loginService.login(username,password);
        }catch (Exception e) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过",e.getMessage());
            System.out.println(e.getMessage());
            throw new AuthenticationException(e.getMessage());
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,password,getName());
        return info;
    }
    //清理缓存权限
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
