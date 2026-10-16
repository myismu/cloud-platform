package com.exambaby.common.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文工具类
 * 
 * 功能说明：
 * 获取 Spring 的 ApplicationContext，提供静态方法获取 Bean
 * 
 * 使用场景：
 * - 在非 Spring 管理的类中获取 Bean
 * - 在工具类中获取 Spring 容器中的服务
 * 
 * 使用方式：
 * SpringContextUtils.getBean(SomeService.class)
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    
    private static ApplicationContext context;

    /**
     * 实现 ApplicationContextAware 接口，设置 ApplicationContext
     * Spring 容器启动时会自动调用此方法
     * 
     * @param ctx ApplicationContext 应用程序上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

    /**
     * 根据类型获取 Bean 实例
     * 
     * @param clazz Bean 的 Class 类型
     * @param <T>   Bean 的类型
     * @return Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
