package com.exambaby.api.userhub.dto;

import lombok.Data;

/**
 * 登录响应 DTO
 * 
 * 功能说明：
 * 封装登录成功后的响应数据
 * 
 * 使用场景：
 * - 用户登录成功后返回 Token
 * - 服务间登录响应传递
 */
@Data
public class LoginResponse {
    
    /**
     * JWT Token
     * 用于后续请求的身份认证
     */
    private String token;
}
