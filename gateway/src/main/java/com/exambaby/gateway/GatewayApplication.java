package com.exambaby.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Gateway 网关服务启动类
 * 
 * 功能说明：
 * 1. 作为微服务架构的统一入口
 * 2. 负责路由转发、负载均衡、熔断降级
 * 3. 统一鉴权、日志记录、跨域处理
 * 
 * 注解说明：
 * @SpringBootApplication - Spring Boot 启动类注解
 * @EnableDiscoveryClient - 启用 Nacos 服务发现
 * exclude = {DataSourceAutoConfiguration.class} - 排除数据源自动配置（网关不需要数据库）
 */
@SpringBootApplication(
        scanBasePackages = {"com.exambaby.gateway", "com.exambaby.common"},
        exclude = {DataSourceAutoConfiguration.class}
)
@EnableDiscoveryClient
public class GatewayApplication {
    
    /**
     * 应用程序入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
