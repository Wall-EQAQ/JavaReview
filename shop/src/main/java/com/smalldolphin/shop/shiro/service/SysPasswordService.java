package com.smalldolphin.shop.shiro.service;

import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.common.constant.ShiroConstants;
import com.smalldolphin.shop.common.exception.BaseException;
import com.smalldolphin.shop.common.redis.RedisCache;
import com.smalldolphin.shop.config.AsyncFactory;
import com.smalldolphin.shop.config.AsyncManage;
import com.smalldolphin.shop.domain.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SysPasswordService {

    @Autowired
    private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private String maxRetryCount;

    private final String SYS_LOGINRECORDCACHE_KEY = "sys_loginRecordCache:";

    /**
     *  设置 cache key
     * @param loginName
     * @return  缓存键key
     */
    private String getCacheKey(String loginName) {
        return SYS_LOGINRECORDCACHE_KEY + loginName;
    }

    /**
     * Md5Hash.toHex()返回Md5加密之后的16进制  toString()是直接返回加密后的字符串
     * @param username
     * @param password
     * @param salt
     * @return
     */
    public String encryptPassword(String username, String password, String salt) {
        return new Md5Hash(username + password + salt).toHex();
    }

    public void validate(SysUser user, String password) {
        String loginName = user.getLoginName();
        Integer retryCount = redisCache.getCacheObject(getCacheKey(loginName));

        //缓存中没有，则初始化retryCount为 0
        if(retryCount == null) {
            retryCount = 0;
            redisCache.setCacheObject(getCacheKey(loginName), retryCount, 10, TimeUnit.MINUTES);
        }
        if (retryCount > Integer.valueOf(maxRetryCount).intValue()) {
            AsyncManage.async().execute(AsyncFactory.recordLoginInfo(loginName, Constants.LOGIN_FAIL, "user.password.retry.limit.exceed"));
        }
        if (!match(user,password)) {
            AsyncManage.async().execute(AsyncFactory.recordLoginInfo(loginName, Constants.LOGIN_FAIL,"登录失败，账号密码不匹配"));
            redisCache.setCacheObject(getCacheKey(loginName), retryCount, 10, TimeUnit.MINUTES);
            throw new BaseException("账号密码不匹配");
        }else {
            clearLoginRecordCache(loginName);
        }

    }

    /**
     * 根据当前的登录用户名，盐值加密后与数据库中的密码进行比对
     * @param user
     * @param newPassword
     * @return
     */
    public boolean match(SysUser user, String newPassword) {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }


    public void clearLoginRecordCache(String loginName) {
        redisCache.deleteObject(getCacheKey(loginName));
    }

    //重置密码的方法
    public static void main(String[] args) {
        System.out.println(new SysPasswordService().encryptPassword("admin","admin123","111111"));
        System.out.println(new SysPasswordService().encryptPassword("ry","admin123","22222"));
    }











}
