package com.exambaby.gateway.filter;

import com.exambaby.common.core.utils.ThreadLocalContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * 请求信息过滤器（RequestInfoFilter）
 * 
 * ============================================================
 * 功能说明：
 * 提取请求中的客户端信息（IP地址、User-Agent等）并存储到 ThreadLocal 中
 * 供后续的过滤器和业务逻辑使用
 * 
 * ============================================================
 * 在过滤器链中的位置：
 * 执行顺序：-200（最先执行，确保后续过滤器可以获取到请求信息）
 * 
 * ============================================================
 * 工作流程图：
 * 
 *   请求进入网关
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 1. 获取客户端 IP 地址                │
 *   │    - 优先从 X-Forwarded-For 获取     │
 *   │    - 否则从远程地址获取              │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 2. 获取 User-Agent 信息             │
 *   │    - 浏览器类型、版本等              │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 3. 存储到 ThreadLocalContext        │
 *   │    - setIp(ip)                      │
 *   │    - setUserAgent(userAgent)        │
 *   └─────────────────────────────────────┘
 *        ↓
 *   继续执行下一个过滤器
 * 
 * ============================================================
 * 为什么需要最先执行？
 * 因为后续的 AuthFilter、GlobalLogFilter 等都需要使用客户端信息
 * 必须先提取并存储，其他过滤器才能直接使用
 * ============================================================
 */
@Component
public class RequestInfoFilter implements GlobalFilter, Ordered {

    /**
     * 过滤器核心方法
     * 
     * @param exchange ServerWebExchange 对象，包含请求和响应信息
     * @param chain    GatewayFilterChain 过滤器链
     * @return Mono<Void> 异步响应
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 提取客户端 IP 地址
        String ip = getClientIp(request);
        
        // 提取 User-Agent（浏览器/客户端信息）
        String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);

        // 存储到 ThreadLocal，供后续过滤器使用
        ThreadLocalContext.setIp(ip);
        ThreadLocalContext.setUserAgent(userAgent);

        // 继续执行过滤器链
        return chain.filter(exchange);
    }

    /**
     * 获取客户端真实 IP 地址
     * 
     * 考虑到请求可能经过代理（如 Nginx、负载均衡器等），
     * 需要从 X-Forwarded-For 头获取真实客户端 IP
     * 
     * X-Forwarded-For 格式：client, proxy1, proxy2
     * 第一个 IP 就是真实客户端 IP
     * 
     * @param request HTTP 请求对象
     * @return 客户端 IP 地址
     */
    private String getClientIp(ServerHttpRequest request) {
        // 优先从代理头获取
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        
        // 如果没有代理头，或值为空/unknown，则从远程地址获取
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                ip = remoteAddress.getAddress().getHostAddress();
            }
        }
        
        // 如果有多个 IP（经过多个代理），取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

    /**
     * 过滤器执行顺序
     * 
     * 值越小，优先级越高
     * -200 表示最先执行
     * 
     * @return 执行顺序值
     */
    @Override
    public int getOrder() {
        return -200;
    }
}
