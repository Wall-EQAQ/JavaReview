package com.smalldolphin.shop.shiro.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldolphin.shop.common.constant.ShiroConstants;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.utils.AjaxResult;
import com.smalldolphin.shop.utils.ServletUtil;
import com.smalldolphin.shop.utils.ShiroUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Description:    登录账号控制过滤器
 * @Created by Administrator on 2021/8/4 9:04
 * @Modified by:
 */
public class KickoutSessionFilter extends AccessControlFilter {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    //同一个用户最大会话数  -1表示无限制
    private int maxSession = -1;
    //后登录的用户是否提出前面登录的   默认false  是踢出的
    private Boolean kickoutAfter = false;
    //踢出后到的地址
    private String kickoutUrl;

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    /**
     *  允许访问返回true,否则返回false
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    /**
     *  当访问拒绝时，是否已经处理， 返回true,表示需要继续处理，返回false，表示已经处理了直接返回即可
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered() || maxSession == -1) {
            //如果没有登录   或用户最大会话数为 -1 则直接进行之后的流程
            return true;
        }
        try{
            Session session = subject.getSession();
            //当前登录用户
            SysUser user = ShiroUtil.getUser();
            String loginName = user.getLoginName();
            Serializable sessionId = session.getId();
            //读取缓存优化   没有就存入
            Deque<Serializable> deque = cache.get(loginName);
            if (deque == null) {
                //初始化队列
                deque = new ArrayDeque<>();
            }
            //如果队列里没有此sessionId 且优化没有被踢出，放入队列
            if (deque.contains(sessionId) && session.getAttribute("kickout") == null) {
                //放入队列
                deque.push(sessionId);
                //将用户的sessionId队列缓存
                cache.put(loginName, deque);
            }

            //如果队列里的sessionId数超出最大会话数， 开始踢人
            while (deque.size() > maxSession) {
                Serializable kickoutSessionId = null;
                if (kickoutAfter) {
                    kickoutSessionId =deque.removeFirst();
                }else {
                    kickoutSessionId = deque.removeLast();
                }
                cache.put(loginName, deque);

                try {
                   Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                    if (null != kickoutSession) {
                        kickoutSession.setAttribute("kickout", true);
                    }
                }catch (Exception e) {
                    //面对异常我们选择忽略
                }
            }
            //  如果被踢出了，重定向到踢出后的地址
            if ((Boolean) session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout") == true) {
                subject.logout();
                saveRequest(request);
                return isAjaxResponse(request, response);
            }
            return true;
        }catch (Exception e) {
            return isAjaxResponse(request, response);
        }
    }

    private boolean isAjaxResponse(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (ServletUtil.isAjaxRequest(req)) {
            AjaxResult ajaxResult = AjaxResult.error("您已在别处登录，请您修改密码或重新登录");
            ServletUtil.renderString(res, objectMapper.writeValueAsString(ajaxResult));

        }else {
            WebUtils.issueRedirect(request, response, kickoutUrl);
        }
        return false;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setKickoutAfter(Boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
    }
}
