package com.smalldolphin.shop.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.shiro.AuthRealm;
import com.smalldolphin.shop.shiro.service.SysPasswordService;
import com.smalldolphin.shop.shiro.session.OnlineSessionFactory;
import com.smalldolphin.shop.shiro.web.filter.*;
import com.smalldolphin.shop.utils.CipherUtil;
import com.smalldolphin.shop.utils.StringUtil;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Value("${shiro.session.expireTime}")
    private int expireTime;

    @Value("${shiro.session.maxSession}")
    private int maxSession;

    @Value("${shiro.session.kickoutAfter}")
    private boolean kickoutAfter;

    @Value("${shiro.user.captchaEnabled}")
    private boolean captchaEnabled;

    @Value("${shiro.user.captchaType}")
    private String captchaType;

    @Value("${shiro.cookie.domain}")
    private String domain;

    @Value("${shiro.cookie.path}")
    private String path;

    @Value("${shiro.cookie.httpOnly}")
    private boolean httpOnly;

    @Value("${shiro.cookie.maxAge}")
    private int maxAge;

    @Value("${shiro.cookie.cipherKey}")
    private String cipherKey;

    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    @Value("${shiro.user.successUrl}")
    private String successUrl;

    @Value("${shiro.user.unauthorizedUrl}")
    private String unauthorizedUrl;

    @Value("${shiro.rememberMe.enabled: false}")
    private boolean rememberMe;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.database}")
    private int dataBase;

    @Value("${spring.redis.password}")
    private String password;



    @Autowired
    SysPasswordService passwordService;

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存 一个登陆用户(AdminCertCardNoToken)的标识
        redisCacheManager.setPrincipalIdFieldName("userId");
        return redisCacheManager;
    }

    @Bean("redisManager")
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" + redisPort);
        redisManager.setDatabase(dataBase);
        if (StringUtil.isNotEmpty(password)) {
            redisManager.setPassword(password);
        }
        redisManager.setTimeout(expireTime * 60);
        return redisManager;
    }


    @Bean("authRealm")
    public AuthRealm authRealm() {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setAuthorizationCacheName(Constants.SYS_AUTH_CACHE);
        authRealm.setCacheManager(redisCacheManager());
        return authRealm;
    }

    /**
     *  RedisSessionDao
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setExpire(expireTime * 60);
        return redisSessionDAO;
    }

    /**
     *  会话管理器
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        //加入缓存管理器
        manager.setCacheManager(redisCacheManager());
        //去掉 JSESSIONID  源码中，现在默认的就是false
        manager.setSessionIdUrlRewritingEnabled(false);
        //自定义SessionDao
        manager.setSessionDAO(redisSessionDAO());
        return manager;
    }


    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(authRealm);
        //注入缓存管理器
        securityManager.setCacheManager(redisCacheManager());
        //session管理器
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(loginUrl);
        bean.setSuccessUrl(successUrl);
        bean.setUnauthorizedUrl(unauthorizedUrl);
        //相关的配置
        LinkedHashMap<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        //shiro的filter拦截器在Spring的servlet执行之前执行
        filterChainDefinitionMap.put("/img/**","anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/ajax/**","anon");
        filterChainDefinitionMap.put("/login","anon,captchaValidate");
        filterChainDefinitionMap.put("/index","authc");
        //放行验证码
        filterChainDefinitionMap.put("/captcha/captchaImage**","anon");

        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("captchaValidate", captchaValidateFilter());
        filters.put("kickout", kickoutSessionFilter());
        filters.put("logoutFilter", logoutFilter());
        bean.setFilters(filters);
        //所有请求需要认证
        filterChainDefinitionMap.put("/**","user,kickout");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean("captchaValidateFilter")
    public CaptchaValidateFilter captchaValidateFilter() {
        CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
        captchaValidateFilter.setCaptchaEnabled(captchaEnabled);
        captchaValidateFilter.setCaptchaType(captchaType);
        return captchaValidateFilter;
    }



    /**
     *  自定义sessionFactory会话
     * @return
     */
    @Bean
    public OnlineSessionFactory sessionFactory() {
        OnlineSessionFactory sessionFactory = new OnlineSessionFactory();
        return sessionFactory;
    }


    /**
     *  退出过滤器
     * @return
     */
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setLoginUrl(loginUrl);
        return logoutFilter;
    }


    /**
     *  同一个用户多设备登录限制
     * @return
     */
    public KickoutSessionFilter kickoutSessionFilter() {
        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        kickoutSessionFilter.setCacheManager(redisCacheManager());
        kickoutSessionFilter.setSessionManager(sessionManager());
        // 同一个用户最大的会话数，默认-1无限制；比如2的意思是同一个用户允许最多同时两个人登录
        kickoutSessionFilter.setMaxSession(maxSession);
        // 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序
        kickoutSessionFilter.setKickoutAfter(kickoutAfter);
        //被踢出后重定向的地址
        kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionFilter;
    }

    /**
     *  cookie属性设置
     * @return
     */
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(maxAge * 24 * 60 * 60);
        return cookie;
    }

    /**
     *  记住我
     * @return
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        if (StringUtil.isNotEmpty(cipherKey)) {
            manager.setCipherKey(Base64.decode(cipherKey));
        }else {
            manager.setCipherKey(CipherUtil.generateNewKey(120, "AES").getEncoded());
        }
        return manager;
    }


    /**
     *Shiro与Thymeleaf的整合
     */
     @Bean
     public ShiroDialect shiroDialect() {
         return new ShiroDialect();
     }

    /**
     * 开启shiro注解通知器
      * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        //取出我们在Spring中自定义的securityManager，注入到advisor中，返回即可
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 解决Shiro注解配置和spring aop一起使用的bug,该bug会导致shiro注解的请求，不能呗映射
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
