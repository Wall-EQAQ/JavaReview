package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.SysConfig;

import java.util.List;

/**
 * @Description:
 * @Created by Administrator on 2022/1/3 22:58
 * @Modified by:
 */
public interface SysConfigMapper {
    /**
     *  查询参数配置信息
     * @param config
     * @return
     */
    SysConfig selectConfig(SysConfig config);

    /**
     *  查询参数配置列表
     * @param config
     * @return
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    public int insertConfig(SysConfig config);

    public int updateConfig(SysConfig config);

    public int deleteConfigById(Long configId);

    public int deleteConfigByIds(String[] ids);

    public SysConfig checkConfigKeyUnique(String configKey);

}
