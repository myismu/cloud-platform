package com.exambaby.common.core.filter;

import com.exambaby.common.core.utils.ThreadLocalContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 请求头认证过滤器（公共模块）
 *
 * 功能说明：
 * 从网关传递的请求头中提取用户信息并存入 ThreadLocal 上下文，
 * 所有下游微服务自动继承此过滤器，无需重复定义。
 *
 * 处理流程：
 * 1. 从请求头 X-User-Id 获取用户ID
 * 2. 从请求头 X-System-Code 获取系统编码
 * 3. 将信息存入 ThreadLocalContext
 * 4. 请求结束后清理 ThreadLocal，防止内存泄漏
 *
 * 注意：
 * - 仅在 Servlet 环境下生效（网关为 WebFlux 环境，自动跳过）
 * - 使用最高优先级确保最先执行
 * - 继承 OncePerRequestFilter 确保每次请求只执行一次
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET) // 仅在 Servlet 环境下生效
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 过滤器核心方法
     * 从请求头提取用户信息并设置到线程上下文
     *
     * @param request     HTTP 请求
     * @param response    HTTP 响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String systemCode = request.getHeader("X-System-Code");

        if (userId != null && !userId.isEmpty()) {
            ThreadLocalContext.setUserId(userId);
        }

        if (systemCode != null && !systemCode.isEmpty()) {
            ThreadLocalContext.setSystemCode(systemCode);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            ThreadLocalContext.clear();
        }
    }
}
