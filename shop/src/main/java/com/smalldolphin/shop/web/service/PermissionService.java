package com.smalldolphin.shop.web.service;

import com.smalldolphin.shop.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Description:    若依的权限
 * @Created by Administrator on 2021/12/27 14:55
 * @Modified by:
 */
@Service("permission")
public class PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);
    //没有权限，hidden用于前端隐藏按钮
    public static final String NOACCESS = "hidden";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";
    //验证用户是否具备某权限，无权限返回hidden用于前端隐藏（如需返回Boolean使用isPermitted）
    public String hasPermi(String permission) {
        return hasPermitted(permission) ? StringUtil.EMPTY : NOACCESS;
    }
    //判断用户是否拥有某个权限
    public boolean hasPermitted(String permission) {
        return SecurityUtils.getSubject().isPermitted(permission);
    }

}
