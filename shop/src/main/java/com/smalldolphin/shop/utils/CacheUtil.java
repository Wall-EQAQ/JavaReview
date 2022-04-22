package com.smalldolphin.shop.utils;

import org.apache.shiro.cache.Cache;

import org.crazycake.shiro.RedisCache;
import org.crazycake.shiro.RedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:    Cache工具类
 * @Created by Administrator on 2021/12/13 22:59
 * @Modified by:
 */
public class CacheUtil {

    private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static RedisCacheManager cacheManager = SpringUtil.getBean(RedisCacheManager.class);

    private static final String SYS_CACHE = "sys-cache";

    /**
     * 获取系统缓存
     * @param key
     * @return
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    public static Object get(String key, Object defaultValue) {
        Object value = key;
        return value != null ? value : defaultValue;
    }

    public static Object get(String cacheName, String key) {
        return getCache(cacheName).get(key);
    }

    /**
     *  获得一个Cache,没有则显示日志
     * @param cacheName
     * @return
     */
    private static RedisCache<String, Object> getCache(String cacheName) {
        //根据缓存名称获取一个Cache实例
        Cache<String, Object> cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new RuntimeException("当前系统中没有定义" + cacheName + "这个缓存");
        }
        return (RedisCache<String, Object>) cache;
    }

    /**
     *  写入缓存
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value) {
        getCache(cacheName).put(key, value);
    }
    //写入SYS_CACHE 缓存
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     *  从SYS_CACHE缓存中移除
     * @param key
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     *  从缓存中移除
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }




}
