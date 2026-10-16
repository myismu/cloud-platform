package com.exambaby.common.core.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 客户端类型解析工具类
 * 
 * 功能说明：
 * 根据请求的 User-Agent 解析客户端类型，以及获取客户端真实 IP 地址
 * 
 * 支持的客户端类型：
 * - 测试平台（Postman、Apifox）
 * - PC 端（Windows、Mac）
 * - 移动端（Android、iPhone）
 * - 微信客户端
 */
public class ClientTypeUtil {

    /**
     * 解析客户端类型
     * 根据 User-Agent 判断客户端类型
     * 
     * @param userAgent 客户端 User-Agent 字符串
     * @return 客户端类型描述
     */
    public static String resolveClientType(String userAgent) {
        if (userAgent == null) return "UNKNOWN";
        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("postman")) {
            return "测试平台Postman";
        } else if (userAgent.contains("apifox")) {
            return "测试平台Apifox";
        } else if (userAgent.contains("windows") || userAgent.contains("macintosh")) {
            return "PC";
        } else if (userAgent.contains("android") || userAgent.contains("iphone")) {
            return "Mobile";
        } else if (userAgent.contains("micromessenger")) {
            return "WeChat";
        }

        return "UNKNOWN";
    }

    /**
     * 获取客户端真实 IP 地址
     * 优先从代理头获取，否则从远程地址获取
     * 
     * @param request HTTP 请求对象
     * @return 客户端 IP 地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}