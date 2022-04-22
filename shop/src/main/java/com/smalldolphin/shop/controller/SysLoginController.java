package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.utils.AjaxResult;
import com.smalldolphin.shop.utils.ServletUtil;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:shop
 * @Created by Administrator on 2021/3/29 22:01
 * @Modified by:
 */

@Controller
public class SysLoginController extends BaseController {


    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtil.isAjaxRequest(request)) {
            return ServletUtil.renderString(response,"{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        return "login";
    }

    /**
     *  认证登录的方法
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
        UsernamePasswordToken upToken = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try{
            //login()方法登录成功会返回AuthenticationToken信息,认证失败回抛出异常
            subject.login(upToken);
            return AjaxResult.success();
        }catch (AuthenticationException e) {
            String msg = "用户名或者密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return AjaxResult.error(msg);
        }
    }

    /**
     *  没有权限(未登录)
     * @return
     */
    @RequestMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }
}
