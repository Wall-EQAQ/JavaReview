package com.smalldolphin.shop.shiro.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.common.constant.ShiroConstants;
import com.smalldolphin.shop.common.exception.BaseException;
import com.smalldolphin.shop.config.AsyncFactory;
import com.smalldolphin.shop.config.AsyncManage;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.service.SysUserService;
import com.smalldolphin.shop.utils.ServletUtil;
import com.smalldolphin.shop.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/15 15:01
 * @Modified by:
 */
@Component
public class SysLoginService {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    public SysUser login(String username, String password) {
        System.out.println("11~~~~");
        Object attribute = ServletUtil.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA);
        System.out.println("22~~~~");
        System.out.println(attribute);
        System.out.println("33~~~~");
        if (!ObjectUtil.isEmpty(attribute)) {
            AsyncManage.async().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, "captcha error" ));
            throw new BaseException("captcha error");
        }
        //用户名或者密码为空  错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManage.async().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, "username or password is empty" ));
            throw new BaseException("username or password is empty");
        }
        /**
         * 用户名 不在指定范围内
         * 密码 不在指定范围内  TODO
         */

        SysUser user = userService.selectUserByLoginName(username);
        if (user == null) {
            AsyncManage.async().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, "user not exist"));
            throw new BaseException("user not exist");
        }
        //数据库中的密码和用户输入的密码进行匹配,校验出错，就会抛出异常
        passwordService.validate(user,password);
        AsyncManage.async().execute(AsyncFactory.recordLoginInfo(username,Constants.LOGIN_SUCCESS,"登录成功"));
        recordLogin(user);
        return user;

    }


    //记录登录信息,登录的IP地址和时间
    public void recordLogin(SysUser user) {
        user.setLoginIp(SecurityUtils.getSubject().getSession().getHost());
        user.setLoginDate(new Date());
        userService.updateUser(user);
    }
}
