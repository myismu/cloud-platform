package com.exambaby.gateway.filter;

import com.exambaby.common.core.entity.GlobalLog;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.gateway.service.GatewayLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 全局日志过滤器（GlobalLogFilter）
 * 
 * ============================================================
 * 功能说明：
 * 记录所有通过网关的请求日志，包括请求路径、方法、响应状态、耗时等信息
 * 用于审计、问题排查、性能监控等场景
 * 
 * ============================================================
 * 在过滤器链中的位置：
 * 执行顺序：-50（在 AuthFilter 之后执行，确保可以获取用户信息）
 * 
 * ============================================================
 * 工作流程图：
 * 
 *   请求进入网关
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 1. 检查是否需要跳过日志记录          │
 *   │    - 静态资源（/img/、/static/等）   │
 *   │    - favicon.ico                    │
 *   └─────────────────────────────────────┘
 *        ↓ 不跳过
 *   ┌─────────────────────────────────────┐
 *   │ 2. 记录请求开始时间                  │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 3. 提取请求信息                      │
 *   │    - 请求方法（GET/POST等）          │
 *   │    - 请求路径                        │
 *   │    - 客户端 IP                       │
 *   │    - User-Agent                     │
 *   │    - 用户ID（从请求头获取）          │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 4. 执行请求（chain.filter）          │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 5. 请求完成后（then 回调）           │
 *   │    - 计算请求耗时                    │
 *   │    - 获取响应状态码                  │
 *   │    - 构建日志对象                    │
 *   │    - 异步保存日志到数据库            │
 *   └─────────────────────────────────────┘
 * 
 * ============================================================
 * 日志内容说明：
 * - requestUrl: 请求的 URL 路径
 * - requestMethod: HTTP 方法（GET、POST、PUT、DELETE等）
 * - ipAddress: 客户端 IP 地址
 * - userAgent: 浏览器/客户端信息
 * - responseStatusCode: HTTP 响应状态码
 * - responseTime: 请求处理耗时（毫秒）
 * - exceptionMessage: 异常信息（如果请求失败）
 * ============================================================
 */
@Log4j2
@Component
public class GlobalLogFilter implements GlobalFilter, Ordered {

    @Autowired
    private GatewayLogService gatewayLogService;

    /**
     * 完全跳过的路径列表
     * 这些路径的请求不会被记录日志
     */
    private static final List<String> SKIP_PATHS = List.of(
            "/favicon.ico"
    );

    /**
     * 按前缀跳过的路径列表
     * 以这些前缀开头的路径不会被记录日志（通常是静态资源）
     */
    private static final List<String> SKIP_PREFIXES = List.of(
            "/img/",
            "/static/",
            "/css/",
            "/js/",
            "/fonts/",
            "/webjars/"
    );

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
        String path = request.getURI().getPath();

        // 步骤1：检查是否需要跳过日志记录
        if (shouldSkip(path)) {
            return chain.filter(exchange);
        }

        // 步骤2：记录请求开始时间
        long startTime = System.currentTimeMillis();
        
        // 步骤3：提取请求信息
        String method = request.getMethod().name();
        String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
        String ip = getClientIp(request);
        String userName = request.getHeaders().getFirst("X-User-Id");

        // 步骤4：执行请求，并在完成后记录日志
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    // 步骤5：请求完成后计算耗时
                    long duration = System.currentTimeMillis() - startTime;
                    int status = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : 500;

                    // 打印请求日志到控制台
                    log.info("Gateway request: {} {} - {}ms", method, path, duration);

                    // 构建日志对象
                    GlobalLog globalLog = new GlobalLog();
                    globalLog.setSystemId(ThreadLocalContext.getSystemId());
                    globalLog.setUserName(userName);
                    globalLog.setRequestUrl(path);
                    globalLog.setRequestMethod(method);
                    globalLog.setIpAddress(ip);
                    globalLog.setUserAgent(userAgent);
                    globalLog.setResponseStatusCode(status);
                    globalLog.setResponseTime(duration);

                    // 如果请求失败，记录异常信息
                    if (status != 200) {
                        globalLog.setExceptionMessage("Request failed with status: " + status);
                    }

                    // 异步保存日志到数据库
                    gatewayLogService.saveLog(globalLog)
                            .subscribe(
                                    success -> {},
                                    error -> log.error("Failed to save log: {}", error.getMessage())
                            );
                }));
    }

    /**
     * 判断路径是否应该跳过日志记录
     * 
     * 跳过静态资源和不需要记录的路径，减少日志量
     * 
     * @param path 请求路径
     * @return true 表示跳过，false 表示需要记录
     */
    private boolean shouldSkip(String path) {
        // 检查是否是完全匹配的路径
        if (SKIP_PATHS.stream().anyMatch(path::equals)) {
            return true;
        }
        // 检查是否是前缀匹配的路径
        return SKIP_PREFIXES.stream().anyMatch(path::startsWith);
    }

    /**
     * 获取客户端真实 IP 地址
     * 
     * 优先从代理头 X-Forwarded-For 获取，否则从远程地址获取
     * 
     * @param request HTTP 请求对象
     * @return 客户端 IP 地址
     */
    private String getClientIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                ip = remoteAddress.getAddress().getHostAddress();
            }
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 过滤器执行顺序
     * 
     * 值越小，优先级越高
     * -50 表示在认证过滤器之后执行
     * 
     * @return 执行顺序值
     */
    @Override
    public int getOrder() {
        return -50;
    }
}
