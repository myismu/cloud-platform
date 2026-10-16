package com.exambaby.common.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Redis 在线用户信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisOnlineUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 最后访问时间
     */
    private String lastAccessTime;

    /**
     * 浏览器/客户端类型
     */
    private String clientType;

    /**
     * 登录令牌
     */
    private String token;
    
    /**
     * 系统代码 (questionBank / userHub)
     */
    private String systemCode;
}
