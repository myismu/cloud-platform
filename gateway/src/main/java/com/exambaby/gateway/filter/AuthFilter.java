package com.exambaby.gateway.filter;

import com.exambaby.common.core.dto.RedisOnlineUser;
import com.exambaby.common.core.utils.JwtUtil;
import com.exambaby.common.core.utils.ThreadLocalContext;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 认证过滤器（AuthFilter）
 * 
 * ============================================================
 * 功能说明：
 * 对所有进入网关的请求进行 JWT Token 验证，确保只有合法用户才能访问受保护的资源
 * 
 * ============================================================
 * 在过滤器链中的位置：
 * 执行顺序：-100（在 RequestInfoFilter 之后，GlobalLogFilter 之前执行）
 * 
 * ============================================================
 * 工作流程图：
 * 
 *   请求进入网关
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 1. 检查是否在白名单中？              │
 *   │    - 是 → 直接放行，跳过认证         │
 *   │    - 否 → 继续认证流程               │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 2. 提取请求头中的 Token              │
 *   │    格式：Authorization: Bearer xxx   │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 3. Token 是否存在？                  │
 *   │    - 否 → 返回 401 未授权            │
 *   │    - 是 → 继续验证                   │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 4. 解析 JWT Token                    │
 *   │    - 获取用户ID（subject）           │
 *   │    - 获取系统代码（systemCode）      │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 5. 验证 Token 有效性                 │
 *   │    - 检查签名是否正确                │
 *   │    - 检查是否过期                    │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 6. 验证 Redis 在线状态               │
 *   │    - 检查用户是否在线                │
 *   │    - 检查 Token 是否匹配             │
 *   └─────────────────────────────────────┘
 *        ↓
 *   ┌─────────────────────────────────────┐
 *   │ 7. 将用户信息添加到请求头            │
 *   │    - X-User-Id: 用户ID              │
 *   │    - X-System-Code: 系统代码        │
 *   └─────────────────────────────────────┘
 *        ↓
 *   转发到下游微服务
 * 
 * ============================================================
 * 白名单说明：
 * 以下路径不需要 Token 验证，可以直接访问：
 * - /api/auth/login     - 登录接口
 * - /api/auth/captcha   - 验证码接口
 * - /api/auth/register  - 注册接口
 * - /img/**, /image/, /templates/, /projects/ - 静态资源
 * ============================================================
 */
@Log4j2
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 白名单路径列表
     * 
     * 这些路径不需要 Token 验证，可以直接访问
     * 包括：
     * - 登录、注册、验证码等公开接口
     * - 静态资源路径
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/auth/login",
            "/api/auth/captcha",
            "/api/auth/register",
            "/api/users/getUserNameById",
            "/img/",        // 静态资源：用户上传图片
            "/image/",      // 静态资源：AI 生成图片
            "/templates/",  // 静态资源：PPT 模板 JSON 及预览图
            "/projects/"    // 静态资源：PPT 项目 JSON 文件
    );

    /**
     * 过滤器核心方法
     * 
     * 这是 GlobalFilter 接口的唯一方法，每个请求都会经过此方法
     * 
     * @param exchange ServerWebExchange 对象，包含：
     *                 - getRequest(): 获取请求对象
     *                 - getResponse(): 获取响应对象
     *                 - mutate(): 创建可修改的副本
     * 
     * @param chain    GatewayFilterChain 过滤器链，用于：
     *                 - chain.filter(exchange): 继续执行下一个过滤器
     *                 - 返回 Mono<Void>: 异步响应结果
     * 
     * @return Mono<Void> 异步响应，WebFlux 响应式编程的核心类型
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求对象和请求路径
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 步骤1：检查是否在白名单中，白名单直接放行
        if (isWhiteListed(path)) {
            return chain.filter(exchange);
        }

        // 步骤2：从请求头中提取 Token
        // 格式：Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉 "Bearer " 前缀
        }

        // 步骤3：检查 Token 是否存在
        if (token == null || token.isEmpty()) {
            return onError(exchange, "Token is missing", HttpStatus.UNAUTHORIZED);
        }

        try {
            // 步骤4：解析 JWT Token，获取声明信息
            Claims claims = jwtUtil.parseToken(token);
            
            // 获取系统代码，用于多租户隔离
            String systemCode = claims.get("systemCode", String.class);
            if (systemCode == null) {
                return onError(exchange, "System code is missing", HttpStatus.UNAUTHORIZED);
            }

            // 将系统代码存入 ThreadLocal，供后续使用
            ThreadLocalContext.setSystemCode(systemCode);

            // 步骤5：验证 Token 有效性（签名、过期时间等）
            if (!jwtUtil.validateToken(token)) {
                return onError(exchange, "Token is invalid", HttpStatus.UNAUTHORIZED);
            }

            // 步骤6：验证 Redis 在线状态
            // Redis Key 格式：online:{systemCode}:{userId}
            String userId = claims.getSubject();
            String redisKey = "online:" + systemCode + ":" + userId;
            RedisOnlineUser redisUser = (RedisOnlineUser) redisTemplate.opsForValue().get(redisKey);

            // 检查用户是否在线，以及 Token 是否匹配
            if (redisUser == null || !redisUser.getToken().equals(token)) {
                return onError(exchange, "Token is invalid or expired", HttpStatus.UNAUTHORIZED);
            }

            // 步骤7：将用户信息添加到请求头，传递给下游微服务
            // 下游服务可以通过 @RequestHeader 获取这些信息
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)           // 用户ID
                    .header("X-System-Code", systemCode)   // 系统代码
                    .build();

            // 继续执行过滤器链，将修改后的请求传递下去
            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("Auth filter error: {}", e.getMessage());
            return onError(exchange, "Authentication failed", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 检查路径是否在白名单中
     * 
     * 使用 startsWith 匹配，支持路径前缀匹配
     * 例如：/img/avatar.png 会匹配 /img/
     * 
     * @param path 请求路径
     * @return true 表示在白名单中，跳过认证；false 表示需要认证
     */
    private boolean isWhiteListed(String path) {
        for (String whitePath : WHITE_LIST) {
            if (path.startsWith(whitePath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回错误响应
     * 
     * 当认证失败时，构造 JSON 格式的错误响应返回给客户端
     * 
     * @param exchange 服务器交换对象
     * @param message  错误信息
     * @param status   HTTP 状态码
     * @return Mono<Void> 异步响应
     */
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构造 JSON 响应体
        String body = String.format("{\"code\":%d,\"message\":\"%s\"}", status.value(), message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 过滤器执行顺序
     * 
     * Ordered 接口方法，返回值越小，优先级越高
     * 
     * 执行顺序设计：
     * - RequestInfoFilter: -200  （最先执行，提取请求信息）
     * - AuthFilter: -100          （本过滤器，进行认证）
     * - GlobalLogFilter: -50      （最后执行，记录日志）
     * 
     * @return 执行顺序值
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
