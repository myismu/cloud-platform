package com.exambaby.gateway.filter;

import com.exambaby.gateway.handler.GatewayFallbackHandler;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 服务不可用过滤器（ServiceUnavailableFilter）
 * 
 * ============================================================
 * 功能说明：
 * 拦截服务不可用的异常，返回友好的错误提示信息
 * 当下游微服务不可用或找不到时，避免返回原始的 404 错误
 * 
 * ============================================================
 * 在过滤器链中的位置：
 * 执行顺序：-100（与 AuthFilter 同级，处理异常）
 * 
 * ============================================================
 * 工作流程图：
 * 
 *   请求进入网关
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 执行过滤器链（chain.filter）         │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 是否抛出 NotFoundException？         │
 *   │    - 是 → 处理服务不可用             │
 *   │    - 否 → 正常返回                   │
 *   └─────────────────────────────────────┘
 *        ↓ 抛出异常
 *   ┌─────────────────────────────────────┐
 *   │ 1. 从路径提取服务名称                │
 *   │    /api/auth/** → 用户服务          │
 *   │    /api/public/** → 题库服务        │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 2. 调用 FallbackHandler 返回错误    │
 *   │    返回友好的错误提示信息            │
 *   └─────────────────────────────────────┘
 * 
 * ============================================================
 * 触发场景：
 * - 下游微服务未启动
 * - 服务注册中心中找不到服务实例
 * - 路由配置错误导致找不到目标服务
 * ============================================================
 */
@Component
public class ServiceUnavailableFilter implements GlobalFilter, Ordered {

    private final GatewayFallbackHandler fallbackHandler;

    public ServiceUnavailableFilter(GatewayFallbackHandler fallbackHandler) {
        this.fallbackHandler = fallbackHandler;
    }

    /**
     * 过滤器核心方法
     * 
     * 使用 onErrorResume 捕获 NotFoundException 异常
     * 当服务不可用时，返回友好的错误提示
     * 
     * @param exchange ServerWebExchange 对象
     * @param chain    GatewayFilterChain 过滤器链
     * @return Mono<Void> 异步响应
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(NotFoundException.class, e -> {
                    // 获取请求路径
                    String path = exchange.getRequest().getPath().value();
                    
                    // 从路径中提取服务名称
                    String serviceName = extractServiceName(path);
                    
                    // 调用 FallbackHandler 处理服务不可用情况
                    return fallbackHandler.handle(exchange, serviceName);
                });
    }

    /**
     * 从请求路径中提取服务名称
     * 
     * 根据路径前缀判断请求应该路由到哪个服务
     * 用于在错误提示中显示具体是哪个服务不可用
     * 
     * @param path 请求路径
     * @return 服务名称（中文）
     */
    private String extractServiceName(String path) {
        // 用户服务相关路径
        if (path.startsWith("/api/auth")
                || path.startsWith("/api/users")
                || path.startsWith("/api/roles")
                || path.startsWith("/api/routes")
                || path.startsWith("/api/systemInfo")
                || path.startsWith("/api/login-history")
                || path.startsWith("/api/password-history")
                || path.startsWith("/api/redis")) {
            return "用户服务";
        }
        
        // 题库服务相关路径
        if (path.startsWith("/api/public")
                || path.startsWith("/api/file")
                || path.startsWith("/api/admin")) {
            return "题库服务";
        }
        
        // 默认返回通用服务名称
        return "服务";
    }

    /**
     * 过滤器执行顺序
     * 
     * 值越小，优先级越高
     * -100 表示与 AuthFilter 同级执行
     * 
     * @return 执行顺序值
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
