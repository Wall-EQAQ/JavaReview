package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.domain.Menu;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.service.MenuService;
import com.smalldolphin.shop.utils.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/15 20:47
 * @Modified by:
 */
@Controller
public class IndexController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/index")
    public String index(ModelMap modelMap) {
        //获取当前登录用户信息
        SysUser user = ShiroUtil.getUser();
        List<Menu> menus = menuService.selectMenuByUser(user);
        modelMap.put("menus", menus);
        modelMap.put("user", user);
        return "index";
    }

    @GetMapping("/system/main")
    public String main() {
        return "main";
    }

}
