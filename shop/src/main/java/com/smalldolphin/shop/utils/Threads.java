package com.smalldolphin.shop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description:    线程相关工具类
 * @Created by Administrator on 2021/7/29 22:06
 * @Modified by:
 */
public class Threads {
    private static final Logger log = LoggerFactory.getLogger(Threads.class);

    /**
     *  停止线程池
     *  先使用shutdown,停止接收新任务并尝试完成所有已存在任务
     *  如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     *  如果仍然超時，則強制退出
     *  对在shutdown时线程本身被调用中断做了处理
     * @param pool
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120,TimeUnit.SECONDS)) {
                        log.info("Pool did not terminate");
                    }
                }
            }catch (InterruptedException ie) {
                pool.shutdownNow();
                //只对阻塞线程起作用，  当线程阻塞时，调用interrupt方法后，该线程会得到一个interrupt异常，可以通过对该异常的处理而退出线程
                Thread.currentThread().interrupt();
            }
        }
    }

}
