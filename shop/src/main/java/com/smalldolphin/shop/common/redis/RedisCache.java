package com.smalldolphin.shop.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description:shop
 * @Created by Administrator on 2022/4/11 11:22
 * @Modified by:
 */

@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  缓存基本对象， Integer,String, 实体类等
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }


    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     *  获取缓存的基本对象
     * @param key
     * @param <T>
     * @return  缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     *  根据key获取缓存中的 val+1
     * @param key
     * @return
     */
    public Long increment(final String key) {
        return redisTemplate.boundValueOps(key).increment();
    }

    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     *  设置有效时间
     * @param key   redis键
     * @param timeout   超时时间
     * @param unit  时间单位
     * @return  true = 设置成功  false = 设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     *  删除单个对象
     * @param key
     * @return
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     *  删除集合对象
     * @param collection    多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     *  缓存list数据
     * @param key   缓存的键值
     * @param dataList  待缓存的List数据
     * @param <T>
     * @return
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPush(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     *  获得缓存的list对象
     * @param key
     * @param <T>
     * @return
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);

    }

    /**
     *  缓存set
     * @param key
     * @param dataSet
     * @param <T>
     * @return  缓存的数据对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperations = redisTemplate.boundSetOps(key);
        Iterator<T> iterator = dataSet.iterator();
        while (iterator.hasNext()) {
            setOperations.add(iterator.next());
        }
        return setOperations;
    }

    /**
     * `获得缓存的key
     * @param key
     * @param <T>
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     *  缓存map
     * @param key
     * @param dataMap
     * @param <T>
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     *  获得缓存的map
     * @param key
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     *  往Hash中存入数据
     * @param key
     * @param hKey
     * @param value
     * @param <T>
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     *  获取Hash中的数据
     * @param key
     * @param hKey
     * @param <T>
     * @return
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     *  获取多个Hash中的数据
     * @param key
     * @param hKeys
     * @param <T>
     * @return
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     *  获得缓存的基本对象列表
     * @param pattern
     * @return
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }












}
