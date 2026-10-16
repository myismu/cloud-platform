package com.exambaby.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient 配置类
 * 
 * 功能说明：
 * 配置支持负载均衡的 WebClient.Builder，用于网关调用后端服务
 * 
 * 使用场景：
 * - 网关调用后端服务的内部接口（如日志保存）
 * - 支持 lb:// 协议的服务发现
 */
@Configuration
public class WebClientConfig {

    /**
     * 创建支持负载均衡的 WebClient 构建器
     * 
     * @return WebClient.Builder 实例
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
