package com.exambaby.common.core.utils;

/**
 * 线程上下文工具类
 * 
 * 功能说明：
 * 使用 ThreadLocal 保存当前请求线程中的上下文信息
 * 
 * 存储的信息：
 * - systemId: 系统ID
 * - systemCode: 系统代码
 * - userAgent: 用户代理信息
 * - ip: 客户端IP地址
 * - userId: 用户ID
 * 
 * 使用场景：
 * - 在过滤器中设置请求信息
 * - 在业务代码中获取请求上下文
 * - 请求结束后清理上下文
 * 
 * 注意事项：
 * 每次请求结束后必须调用 clear() 方法清理 ThreadLocal，防止内存泄漏
 */
public class ThreadLocalContext {
    private static final ThreadLocal<Long> systemIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> systemCodeHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userAgentHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> ipHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    /**
     * 设置系统ID
     * 
     * @param systemId 系统ID
     */
    public static void setSystemId(Long systemId) {
        systemIdHolder.set(systemId);
    }

    /**
     * 获取系统ID
     * 
     * @return 系统ID
     */
    public static Long getSystemId() {
        return systemIdHolder.get();
    }

    /**
     * 设置系统代码
     * 
     * @param systemCode 系统代码
     */
    public static void setSystemCode(String systemCode) {
        systemCodeHolder.set(systemCode);
    }

    /**
     * 获取系统代码
     * 
     * @return 系统代码
     */
    public static String getSystemCode() {
        return systemCodeHolder.get();
    }

    /**
     * 设置用户代理信息
     * 
     * @param userAgent 用户代理信息
     */
    public static void setUserAgent(String userAgent) {
        userAgentHolder.set(userAgent);
    }

    /**
     * 获取用户代理信息
     * 
     * @return 用户代理信息
     */
    public static String getUserAgent() {
        return userAgentHolder.get();
    }

    /**
     * 设置客户端IP地址
     * 
     * @param ip 客户端IP地址
     */
    public static void setIp(String ip) {
        ipHolder.set(ip);
    }

    /**
     * 获取客户端IP地址
     * 
     * @return 客户端IP地址
     */
    public static String getIp() {
        return ipHolder.get();
    }

    /**
     * 设置用户ID
     * 
     * @param userId 用户ID
     */
    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public static String getUserId() {
        return userIdHolder.get();
    }

    /**
     * 清理所有 ThreadLocal 变量
     * 必须在请求结束时调用，防止内存泄漏
     */
    public static void clear() {
        systemIdHolder.remove();
        userAgentHolder.remove();
        ipHolder.remove();
        systemCodeHolder.remove();
        userIdHolder.remove();
    }
}
