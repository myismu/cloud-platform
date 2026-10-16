package com.exambaby.api.userhub.dto;

import lombok.Data;

/**
 * 登录请求 DTO
 * 
 * 功能说明：
 * 封装用户登录请求的参数
 * 
 * 使用场景：
 * - 用户登录时传递登录信息
 * - 服务间登录请求传递
 */
@Data
public class LoginRequest {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 系统代码（用于区分不同系统）
     */
    private String systemCode;
}
