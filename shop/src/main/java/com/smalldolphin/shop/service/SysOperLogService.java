package com.smalldolphin.shop.service;

import com.smalldolphin.shop.domain.SysOperLog;
import com.smalldolphin.shop.mapper.SysOperLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:    操作日志
 * @Created by Administrator on 2022/4/14 0:03
 * @Modified by:
 */
@Service
public class SysOperLogService {

    @Autowired
    private SysOperLogMapper operLogMapper;


    /**
     *  新增操作日志
     * @param operLog
     */
    public void insertOperLog(SysOperLog operLog) {
        operLogMapper.insertOperlog(operLog);
    }

}
