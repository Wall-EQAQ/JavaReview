package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.service.SysUserService;
import com.smalldolphin.shop.shiro.service.SysPasswordService;
import com.smalldolphin.shop.utils.ShiroUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:    用户个人信息  业务处理
 * @Created by Administrator on 2021/8/10 21:20
 * @Modified by:
 */
@Controller
@RequestMapping("/system/user/profile")
public class UserProfileController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @GetMapping("/checkPassword")
    public boolean checkPassword(String password) {
        SysUser user = ShiroUtil.getUser();
        if (passwordService.match(user, password)) {
            return true;
        }
        return false;
    }
}
