package com.smalldolphin.shop.shiro.session;

import com.smalldolphin.shop.utils.IpUtil;
import com.smalldolphin.shop.utils.ServletUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:    自定义sessionFactory 会话
 * @Created by Administrator on 2021/7/25 22:22
 * @Modified by:
 */
@Component
public class OnlineSessionFactory implements SessionFactory {
    @Override
    public Session createSession(SessionContext initData) {
        OnlineSession session = new OnlineSession();
        if (initData != null && initData instanceof WebSessionContext) {
            WebSessionContext sessionContext = (WebSessionContext) initData;
            HttpServletRequest request =(HttpServletRequest) sessionContext.getServletRequest();
            if (request != null) {
                UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtil.getRequest().getHeader("User-Agent"));
                //获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                String browser = userAgent.getBrowser().getName();
                session.setHost(IpUtil.getIpAddr(request));
                session.setBrowser(browser);
                session.setOs(os);

            }
        }
        return session;
    }
}
