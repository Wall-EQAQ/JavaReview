package com.smalldolphin.shop.service;

import com.smalldolphin.shop.domain.SysLoginInfo;
import com.smalldolphin.shop.mapper.LoginInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/7 20:08
 * @Modified by:
 */
public class LoginInfoService {

    @Autowired
    LoginInfoMapper mapper;

    public int insertLoginInfo(SysLoginInfo loginInfo) {
        return mapper.insertLoginInfo(loginInfo);
    }
}
