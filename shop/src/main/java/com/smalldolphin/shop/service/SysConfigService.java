package com.smalldolphin.shop.service;

import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.exception.BaseException;
import com.smalldolphin.shop.common.redis.RedisCache;
import com.smalldolphin.shop.domain.SysConfig;
import com.smalldolphin.shop.mapper.SysConfigMapper;
import com.smalldolphin.shop.utils.Convert;
import com.smalldolphin.shop.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2022/1/3 22:53
 * @Modified by:
 */

@Service
public class SysConfigService {
    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     *  项目启动时，初始化参数到缓存
     *  被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     *  加载参数缓存数据
     */
    public void loadingConfigCache() {
        List<SysConfig> configList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     *  查询参数配置信息
     * @param configId
     * @return
     */
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     *  根据键名查询参数配置信息
     * @param configKey 参数key
     * @return  参数键值
     */
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtil.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtil.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtil.EMPTY;
    }

    /**
     *  查询参数配置列表
     * @param config
     * @return
     */
    public List<SysConfig> selectConfigList(SysConfig config) {
        return configMapper.selectConfigList(config);
    }

    /**
     *  新增参数配置
     * @param config
     * @return
     */
    public int insertConfig(SysConfig config) {
        int row = configMapper.insertConfig(config);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    public int updateConfig(SysConfig config) {
        int row = configMapper.updateConfig(config);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     *  批量删除配置对象
     * @param ids
     */
    public void deleteConfigByIds(String ids) {
        Long[] configIds = Convert.toLongArray(ids);
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (StringUtil.equals(UserConstants.YES, config.getConfigType())) {
                throw new BaseException("内置参数不能删除");
            }
            configMapper.deleteConfigById(configId);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     *  清空缓存数据
     */
    public void clearConfigCache() {
        Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_CACHE + "*");
        redisCache.deleteObject(keys);
    }

    /**
     *  校验参数键名是否唯一
     * @param config
     * @return 结果
     */
    public String checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtil.isNull(config.getConfigId()) ? -1 : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtil.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return UserConstants.CONFIG_KEY_NOT_UNIQUE;
        }
        return UserConstants.CONFIG_KEY_UNIQUE;
    }

    /**
     * 重置参数缓存数据
     */
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }


    //设置cache key
    private String getCacheKey(String configKey) {
        return Constants.SYS_CONFIG_KEY + configKey;
    }


}
