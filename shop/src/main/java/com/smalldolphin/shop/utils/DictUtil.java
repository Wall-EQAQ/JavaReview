package com.smalldolphin.shop.utils;

import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.common.redis.RedisCache;
import com.smalldolphin.shop.domain.DictData;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @Description:    字典工具类
 * @Created by Administrator on 2021/12/13 22:57
 * @Modified by:
 */
@Component
public class DictUtil {

    public static final String SEPARATOR = ",";

    /**
     *  获取字典缓存
     * @param key 参数键
     * @return  字典数据列表
     */
    public static List<DictData> getDictCache(String key) {
        Object cacheObj = SpringUtil.getBean(RedisCache.class).getCacheObject(getCacheKey(key));
        if (StringUtil.isNotNull(cacheObj)) {
            List<DictData> dictData = StringUtil.cast(cacheObj);
            return dictData;
        }
        return null;
    }

    /**
     *  设置字典缓存
     * @param key
     * @param dictData
     */
    public static void setDictCache(String key, List<DictData> dictData) {
        SpringUtil.getBean(RedisCache.class).setCacheObject(getCacheKey(key), dictData);
    }

    /**
     *  获取 cache name
     * @return  缓存名
     */
    public static String getCacheName() {
        return Constants.SYS_DICT_CACHE;
    }

    /**
     *  获取 cache key
     * @param configKey
     * @return  缓存键
     */
    public static String getCacheKey(String configKey) {
        return Constants.SYS_DICT_KEY + configKey;
    }

    /**
     *  清空字典缓存
     */
    public static void clearDictCache() {
        Collection<String> keys = SpringUtil.getBean(RedisCache.class).keys(Constants.SYS_DICT_KEY + "*");
        SpringUtil.getBean(RedisCache.class).deleteObject(keys);
    }

    /**
     *  删除指定字典缓存
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        CacheUtil.remove(getCacheName(), getCacheKey(key));
    }
}
