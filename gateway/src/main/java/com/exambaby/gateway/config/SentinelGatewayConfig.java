package com.exambaby.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Sentinel 网关配置类
 * 
 * 功能说明：
 * 配置 Sentinel 在网关层面的限流和熔断行为
 * 
 * 主要功能：
 * 1. 自定义限流时的响应内容
 * 2. 当请求被限流时，返回友好的 JSON 格式错误信息
 * 
 * 使用场景：
 * - 系统流量过大时自动限流
 * - 防止系统被突发流量击垮
 */
@Configuration
public class SentinelGatewayConfig {

    /**
     * 初始化方法，在 Bean 创建后执行
     */
    @PostConstruct
    public void init() {
        initBlockHandler();
    }

    /**
     * 初始化限流处理器
     * 
     * 当请求触发限流规则时，返回自定义的 JSON 响应：
     * - code: 429 (HTTP 状态码，表示请求过多)
     * - message: 友好的错误提示信息
     * - data: null (无数据返回)
     */
    private void initBlockHandler() {
        GatewayCallbackManager.setBlockHandler((exchange, t) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 429);
            result.put("message", "系统繁忙，请稍后再试");
            result.put("data", null);

            String body;
            try {
                body = new ObjectMapper().writeValueAsString(result);
            } catch (JsonProcessingException e) {
                body = "{\"code\":429,\"message\":\"系统繁忙，请稍后再试\",\"data\":null}";
            }

            return ServerResponse
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body);
        });
    }
}
