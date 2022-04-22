package com.smalldolphin.shop.config;

import com.smalldolphin.shop.utils.SpringUtil;
import com.smalldolphin.shop.utils.Threads;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/18 21:02
 * @Modified by: 异步任务管理器
 */
public class AsyncManage {


    //操作延迟10毫秒
    private final int OPERATE_DELAY_TIME = 10;

    private ScheduledExecutorService executor = SpringUtil.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncManage() {}

    private static AsyncManage asnyc = new AsyncManage();

    public static AsyncManage async() {
        return asnyc;
    }

    /**
     * 执行任务
     */
    public void execute(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     *  停止任务线程池
     */
    public void shutdown() {
        Threads.shutdownAndAwaitTermination(executor);
    }


}
