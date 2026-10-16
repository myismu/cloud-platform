package com.exambaby.common.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus 配置类
 * 
 * 功能说明：
 * 配置 MyBatisPlus 的各种插件，如分页插件
 * 
 * 注意事项：
 * 此类仅在 MyBatisPlus 类存在时加载
 * Gateway 不使用数据库，因此不会加载此类
 * 
 * 插件说明：
 * - PaginationInnerInterceptor: 分页插件，支持 MySQL 数据库分页查询
 */
@Configuration
@ConditionalOnClass(MybatisPlusInterceptor.class)
public class MyBatisPlusConfig {

    /**
     * 创建 MyBatisPlus 拦截器
     * 注册分页插件，支持 MySQL 数据库的分页查询
     * 
     * @return MybatisPlusInterceptor 拦截器实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return interceptor;
    }

}
