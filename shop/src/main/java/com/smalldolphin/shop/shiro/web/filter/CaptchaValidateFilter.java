package com.smalldolphin.shop.shiro.web.filter;

import com.google.code.kaptcha.Constants;
import com.smalldolphin.shop.common.constant.ShiroConstants;
import com.smalldolphin.shop.utils.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/16 21:09
 * @Modified by: 验证码校验的过滤器
 */
public class CaptchaValidateFilter extends AccessControlFilter {

    private boolean captchaEnabled = true;

    private String captchaType = "char";

    public void setCaptchaEnabled(boolean captchaEnabled)
    {
        this.captchaEnabled = captchaEnabled;
    }

    public void setCaptchaType(String captchaType)
    {
        this.captchaType = captchaType;
    }



    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception
    {
        request.setAttribute(ShiroConstants.CAPTCHA_ENABLED, captchaEnabled);
        request.setAttribute(ShiroConstants.CAPTCHA_TYPE, captchaType);
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 验证码禁用 或不是表单提交 允许访问
        if (captchaEnabled == false || !"post".equals(httpServletRequest.getMethod().toLowerCase()))
        {
            return true;
        }
        System.out.println("验证码校验");
        return validateResponse(httpServletRequest, httpServletRequest.getParameter(ShiroConstants.CURRENT_VALIDATECODE));
    }

    public boolean validateResponse(HttpServletRequest request, String validateCode)
    {
        Object obj = SecurityUtils.getSubject().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String code = String.valueOf(obj != null ? obj : "");
        // 验证码清除，防止多次使用。
        request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(code))
        {
            return false;
        }
        return true;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        request.setAttribute(ShiroConstants.CURRENT_CAPTCHA, ShiroConstants.CAPTCHA_ERROR);
        return true;
    }
}
