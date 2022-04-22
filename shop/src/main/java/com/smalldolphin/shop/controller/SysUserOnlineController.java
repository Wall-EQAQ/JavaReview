package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.annotation.Log;
import com.smalldolphin.shop.common.constant.ShiroConstants;
import com.smalldolphin.shop.common.enums.BusinessType;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.domain.UserOnline;
import com.smalldolphin.shop.domain.page.TableDataInfo;
import com.smalldolphin.shop.utils.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

/**
 * @Description:shop
 * @Created by Administrator on 2022/4/8 23:29
 * @Modified by:
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController{

    private String prefix = "monitor/online";

    @Autowired
    private RedisSessionDAO redisSessionDAO;


    @RequiresPermissions("monitor:online:view")
    @GetMapping
    public String online() {
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserOnline userOnline) {
        String ipaddr = userOnline.getIpaddr();
        String loginName = userOnline.getLoginName();

        TableDataInfo rspData = new TableDataInfo();
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        Iterator<Session> it = sessions.iterator();
        List<UserOnline> sessionList = new ArrayList<>();

        while (it.hasNext()) {
            UserOnline user = getSession(it.next());
            if (StringUtil.isNotNull(user)) {
                if (StringUtil.isNotEmpty(ipaddr) && StringUtil.isNotEmpty(loginName)) {
                    if (StringUtil.equals(ipaddr, user.getIpaddr()) && StringUtil.equals(loginName, user.getLoginName())) {
                        sessionList.add(user);
                    }
                }else if (StringUtil.isNotEmpty(ipaddr)) {
                    if (StringUtil.equals(ipaddr, user.getIpaddr())) {
                        sessionList.add(user);
                    }
                }else if (StringUtil.isNotEmpty(loginName)) {
                    if (StringUtil.equals(loginName, user.getLoginName())) {
                        sessionList.add(user);
                    }
                }else {
                    sessionList.add(user);
                }
            }
        }
        rspData.setRows(sessionList);
        rspData.setTotal(sessions.size());
        return rspData;
    }



    @RequiresPermissions(value = {"monitor:online:batchForceLogout", "monitor:online:forceLogout"}, logical = Logical.OR)
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public AjaxResult batchForceLogout(@RequestBody List<UserOnline> userOnlines) {
        for (UserOnline userOnline : userOnlines) {
            String sessionId = userOnline.getSessionId();
            String loginName = userOnline.getLoginName();
            if (sessionId.equals(ShiroUtil.getSessionId())) {
                return AjaxResult.error("当前登录用户无法强退");
            }
            redisSessionDAO.delete(redisSessionDAO.readSession(sessionId));
            removeUserCache(loginName, sessionId);
        }
        return AjaxResult.success();
    }


    private UserOnline getSession(Session session) {

        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (obj == null) {
            return null;
        }
        if (obj instanceof SimplePrincipalCollection) {
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            obj = spc.getPrimaryPrincipal();

            if (null != obj && obj instanceof SysUser) {
                SysUser user = (SysUser) obj;
                UserOnline userOnline = new UserOnline();
                userOnline.setSessionId(session.getId().toString());
                userOnline.setLoginName(user.getLoginName());
                if (StringUtil.isNotNull(user.getDept()) && StringUtil.isNotEmpty(user.getDept().getDeptName())) {
                    userOnline.setDeptName(user.getDept().getDeptName());
                }
                userOnline.setIpaddr(session.getHost());
                userOnline.setStartTimestamp(session.getStartTimestamp());
                userOnline.setLastAccessTime(session.getLastAccessTime());
                return userOnline;
            }
        }
        return null;
    }

    public void removeUserCache(String loginName, String sessionId) {
        Cache<String, Deque<Serializable>> cache = SpringUtil.getBean(RedisCacheManager.class).getCache(ShiroConstants.SYS_USERCACHE);
        Deque<Serializable> deque = cache.get(loginName);
        if (StringUtil.isEmpty(deque) || deque.size() == 0) {
            return;
        }
        deque.remove(sessionId);
        cache.put(loginName,deque);
    }

}
