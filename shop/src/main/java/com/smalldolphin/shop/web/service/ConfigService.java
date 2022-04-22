package com.smalldolphin.shop.web.service;

import com.smalldolphin.shop.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:shop
 * @Created by Administrator on 2022/1/3 22:52
 * @Modified by:
 */

@Service("config")
public class ConfigService {

    @Autowired
    private SysConfigService configService;

    /**
     *  根据键名查询参数配置信息
     * @param configKey
     * @return
     */
    public String getKey(String configKey) {
        return configService.selectConfigByKey(configKey);
    }
}
