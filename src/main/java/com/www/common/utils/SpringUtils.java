package com.www.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>@Description Spring工具类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:42 </p>
 */
@Component
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    /**
     * <p>@Description 设置ApplicationContext </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:44 </p>
     * @param applicationContext ApplicationContext
     * @return void
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }
    /**
     * <p>@Description 获取applicationContext </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:45 </p>
     * @return org.springframework.context.ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * <p>@Description 获取bean </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:45 </p>
     * @param name
     * @return java.lang.Object
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
}
