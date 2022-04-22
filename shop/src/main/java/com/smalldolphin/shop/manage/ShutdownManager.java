package com.smalldolphin.shop.manage;


import com.smalldolphin.shop.config.AsyncManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @Description:    确保应用退出时，能关闭后台线程
 * @Created by Administrator on 2022/4/14 21:52
 * @Modified by:
 */
@Component
public class ShutdownManager {

    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    // 停止异步执行任务
    private void shutdownAsyncManager() {
        try{
            logger.info("===关闭后台任务线程池===");
            AsyncManage.async().shutdown();
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
