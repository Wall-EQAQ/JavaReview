package com.smalldolphin.shop.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/19 19:36
 * @Modified by:  方便在非Spring管理环境中获取bean
 */

@Component
public final class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {

    /**
     *   Spring应用上下文环境
     */
    //该方法可以获取bean相关的定义信息
    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtil.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     *  获取对象
     */
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    /**
     *   获取类型为requiredType的对象  ???
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        T result = (T) beanFactory.getBean(clz);
        return result;
    }

    /**
     *  获取aop代理对象 ???
     * @param invoker
     * @param <T>
     * @return
     */
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }





}
