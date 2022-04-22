package com.smalldolphin.shop.shiro.web.filter;

import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.common.constant.ShiroConstants;
import com.smalldolphin.shop.config.AsyncFactory;
import com.smalldolphin.shop.config.AsyncManage;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.utils.ShiroUtil;
import com.smalldolphin.shop.utils.SpringUtil;
import com.smalldolphin.shop.utils.StringUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;

/**
 * @Description:    退出过滤器
 * @Created by Administrator on 2021/8/1 22:57
 * @Modified by:
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

    //退出后重定向的地址
    private String loginUrl;

    private Cache<String, Deque<Serializable>> cache;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        try{
            //与SecurityUtils中的getSubject不同
            Subject subject = getSubject(request, response);
            String redirectUrl = getRedirectUrl(request, response, subject);
            try {
                SysUser user = ShiroUtil.getUser();
                if (!StringUtil.isNull(user)) {
                    String loginName = user.getLoginName();
                    //记录用户退出日志
                    AsyncManage.async().execute(AsyncFactory.recordLoginInfo(loginName, Constants.LOGOUT, "user logout success"));
                    //清理缓存


                }
                subject.logout();
            }catch (SessionException se) {
                log.error("logout fail", se);
            }
            issueRedirect(request, response, redirectUrl);
        }catch (Exception e) {
            log.error("Encountered session exception during logout.  This can generally safely be ignored", e);
        }
        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;
    }

    /**
     *  退出跳转URL
     * @param request
     * @param response
     * @param subject
     * @return
     */
    @Override
    public String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        String url = getLoginUrl();
        if (!StringUtil.isEmpty(url)) {
            return url;
        }
        return super.getRedirectUrl(request, response, subject);
    }

    public void removeUserCache(String loginName, String sessionId) {
        Deque<Serializable> deque = cache.get(loginName);
        if (StringUtil.isEmpty(deque) || deque.size() == 0) {
            return;
        }
        deque.remove(sessionId);
        cache.put(loginName, deque);
    }

    /**
     *  设置Cache  key的前缀
     * @param cacheManager
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
    }
}
