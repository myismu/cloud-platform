package com.exambaby.gateway.service;

import com.exambaby.common.core.entity.GlobalLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 网关日志服务（GatewayLogService）
 * 
 * ============================================================
 * 功能说明：
 * 负责将网关请求日志异步保存到对应的微服务数据库中
 * 根据请求路径判断日志应该保存到哪个服务
 * 
 * ============================================================
 * 工作原理：
 * 
 *   GlobalLogFilter 记录日志
 *          ↓
 *   GatewayLogService.saveLog()
 *          ↓
 *   根据请求路径判断目标服务
 *          ↓
 *   ┌─────────────────────────────────────┐
 *   │ /api/auth/** → user-hub-service     │
 *   └─────────────────────────────────────┘
 *          ↓
 *   通过 WebClient 调用内部接口保存日志
 * 
 * ============================================================
 * 为什么日志要保存到各个微服务？
 * - 每个微服务有自己的数据库
 * - 日志与业务数据放在一起便于关联查询
 * - 避免网关服务成为性能瓶颈
 * 
 * ============================================================
 * WebClient 配置说明：
 * - @LoadBalanced: 启用负载均衡，通过 Nacos 发现服务实例
 * - lb://user-hub-service: 使用负载均衡调用用户服务
 * ============================================================
 */
@Log4j2
@Service
public class GatewayLogService {

    /**
     * 用户服务的 WebClient
     * 用于调用 user-hub-service 的内部接口
     */
    private final WebClient userHubClient;

    /**
     * 构造函数，初始化 WebClient
     * 
     * @LoadBalanced 注解启用负载均衡
     * lb:// 协议表示通过服务发现获取服务实例
     * 
     * @param webClientBuilder WebClient 构建器，由 Spring 自动注入
     */
    @Autowired
    public GatewayLogService(@LoadBalanced WebClient.Builder webClientBuilder) {
        this.userHubClient = webClientBuilder
                .baseUrl("lb://user-hub-service")
                .build();
    }

    /**
     * 保存日志到对应的微服务
     * 
     * 根据请求路径判断日志应该保存到哪个服务：
     * - 用户服务路径：/api/auth/**, /api/users/**, /api/roles/** 等
     * 
     * @param globalLog 日志对象，包含请求信息
     * @return Mono<Boolean> 保存结果（true=成功，false=失败）
     */
    public Mono<Boolean> saveLog(GlobalLog globalLog) {
        String requestUrl = globalLog.getRequestUrl();

        // 用户服务相关路径，保存到 user-hub-service
        if (requestUrl.startsWith("/api/auth/")
                || requestUrl.startsWith("/api/users/")
                || requestUrl.startsWith("/api/roles/")
                || requestUrl.startsWith("/api/routes/")
                || requestUrl.startsWith("/api/systemInfo/")
                || requestUrl.startsWith("/api/login-history/")
                || requestUrl.startsWith("/api/password-history/")
                || requestUrl.startsWith("/api/redis/")) {
            return saveLogToUserHub(globalLog);
        }

        // 其他路径不保存日志
        return Mono.just(true);
    }

    /**
     * 保存日志到用户服务
     * 
     * 调用 user-hub-service 的内部接口 /internal/log/save
     * 该接口不在网关路由中暴露，仅供内部调用
     * 
     * @param globalLog 日志对象
     * @return Mono<Boolean> 保存结果
     */
    private Mono<Boolean> saveLogToUserHub(GlobalLog globalLog) {
        return userHubClient.post()
                .uri("/internal/log/save")
                .bodyValue(globalLog)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(e -> log.error("Failed to save log to user-hub-service: {}", e.getMessage()))
                .onErrorReturn(false);
    }
}
