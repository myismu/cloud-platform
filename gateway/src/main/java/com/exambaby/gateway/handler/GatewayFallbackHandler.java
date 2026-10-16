package com.exambaby.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关降级处理器（GatewayFallbackHandler）
 * 
 * ============================================================
 * 功能说明：
 * 当后端服务不可用时，提供统一的降级响应处理
 * 返回友好的错误提示，而不是原始的错误信息
 * 
 * ============================================================
 * 使用场景：
 * - 服务宕机或不可达
 * - 服务发现失败（Nacos 中找不到服务实例）
 * - 网络连接异常
 * - 服务端点不存在
 * 
 * ============================================================
 * 响应格式：
 * - HTTP 状态码: 503 Service Unavailable
 * - Content-Type: application/json
 * - 响应体示例：
 *   {
 *     "code": 503,
 *     "message": "用户服务 服务暂时不可用，请稍后再试",
 *     "data": null
 *   }
 * 
 * ============================================================
 * 调用链路：
 * 
 *   ServiceUnavailableFilter
 *          ↓
 *   捕获 NotFoundException
 *          ↓
 *   GatewayFallbackHandler.handle()
 *          ↓
 *   构造 JSON 响应
 *          ↓
 *   返回给客户端
 * ============================================================
 */
@Component
public class GatewayFallbackHandler {

    /**
     * 处理服务不可用的降级响应
     * 
     * 当下游服务不可用时，构造一个友好的 JSON 响应返回给客户端
     * 
     * @param exchange    ServerWebExchange 对象，包含请求和响应信息
     * @param serviceName 服务名称（如"用户服务"、"题库服务"），用于生成友好的错误提示
     * @return Mono<Void> 异步响应
     */
    public Mono<Void> handle(ServerWebExchange exchange, String serviceName) {
        ServerHttpResponse response = exchange.getResponse();
        
        // 设置 HTTP 状态码为 503 Service Unavailable
        response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        
        // 设置响应类型为 JSON
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构造响应体
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", serviceName + " 服务暂时不可用，请稍后再试");
        result.put("data", null);

        // 将 Map 转换为 JSON 字符串
        String body;
        try {
            body = new ObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            // 如果 JSON 序列化失败，使用默认的错误信息
            body = "{\"code\":503,\"message\":\"服务暂时不可用，请稍后再试\",\"data\":null}";
        }

        // 将字符串包装为 DataBuffer 并写入响应
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
