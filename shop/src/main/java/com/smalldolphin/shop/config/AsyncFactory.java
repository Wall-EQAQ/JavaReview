package com.smalldolphin.shop.config;

import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.domain.SysLoginInfo;
import com.smalldolphin.shop.domain.SysOperLog;
import com.smalldolphin.shop.domain.UserOnline;
import com.smalldolphin.shop.service.LoginInfoService;
import com.smalldolphin.shop.service.SysOperLogService;
import com.smalldolphin.shop.shiro.session.OnlineSession;
import com.smalldolphin.shop.utils.AddrUtil;
import com.smalldolphin.shop.utils.LogUtil;
import com.smalldolphin.shop.utils.ServletUtil;
import com.smalldolphin.shop.utils.SpringUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/15 16:09
 * @Modified by:  异步工厂（产生任务用）
 */
public class AsyncFactory {

    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     *  异步记录用户登录信息
     * @param username
     * @param status
     * @param message
     * @return
     */
    public static TimerTask recordLoginInfo(final String username,final String status, final String message) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtil.getRequest().getHeader("User-Agent"));
        final String ip = SecurityUtils.getSubject().getSession().getHost();
        return new TimerTask() {
            @Override
            public void run() {
                //通过用户发起访问请求的IP获取用户登录的地址
                String address = AddrUtil.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtil.getBlock(ip));
                s.append(LogUtil.getBlock(username));
                s.append(LogUtil.getBlock(status));
                s.append(LogUtil.getBlock(message));
                //StringBuilder.toString将StringBuilder对象转换为String对象
                sys_user_logger.info(s.toString());
                //获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                //获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                //封装对象信息
                SysLoginInfo loginInfo = new SysLoginInfo();
                loginInfo.setLoginName(username);
                loginInfo.setIpaddr(ip);
                loginInfo.setLocation(address);
                loginInfo.setMsg(message);
                loginInfo.setOs(os);
                loginInfo.setBrowser(browser);
                //登录状态判断  这里的StringUtils是common3提供的
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    loginInfo.setStatus(Constants.SUCCESS);
                }else if (Constants.LOGIN_FAIL.equals(status)) {
                    loginInfo.setStatus(Constants.LOGIN_FAIL);
                }
                //将登录信息插入到数据库
                SpringUtil.getBean(LoginInfoService.class).insertLoginInfo(loginInfo);
            }
        };
    }

    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                //远程查询操作地点
                operLog.setOperLocation(AddrUtil.getRealAddressByIP(operLog.getOperIp()));
                SpringUtil.getBean(SysOperLogService.class).insertOperLog(operLog);
            }
        };
    }


}
