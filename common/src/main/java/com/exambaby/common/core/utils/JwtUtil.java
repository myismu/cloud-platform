package com.exambaby.common.core.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JWT 工具类
 * 
 * 功能说明：
 * 提供 JWT Token 的生成、解析和验证功能
 * 
 * Token 结构：
 * - subject: 用户ID
 * - claims: 包含角色列表和系统代码
 * - issuedAt: 签发时间
 * - expiration: 过期时间
 * 
 * 配置项：
 * - jwt.secret: 签名密钥
 * - jwt.expiration: 过期时间（毫秒）
 */
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    /**
     * 生成 JWT Token
     * 
     * @param userId     用户ID
     * @param roles      角色列表
     * @param systemCode 系统代码
     * @return JWT Token 字符串
     */
    public String generateToken(String userId, List<String> roles, String systemCode) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("systemCode", systemCode);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 解析 JWT Token (适配 JJWT 0.12.x)
     */
    public Claims parseToken(String token) {
        // 1. 将字符串密钥转为符合新版规范的 SecretKey 对象
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        // 2. 使用新版 parser() 替代 parserBuilder()
        return Jwts.parser()
                .verifyWith(key) // 替代 setSigningKey()
                .build()
                .parseSignedClaims(token) // 替代 parseClaimsJws()
                .getPayload(); // 替代 getBody()
    }

    /**
     * 从 Token 中获取用户ID
     * 
     * @param token JWT Token 字符串
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 从 Token 中获取系统ID
     * 
     * @param token JWT Token 字符串
     * @return 系统ID
     */
    public String getSystemIdFromToken(String token) {
        return (String) parseToken(token).get("systemId");
    }

    /**
     * 从 Token 中获取角色列表
     * 
     * @param token JWT Token 字符串
     * @return 角色列表
     */
    public List<String> getRolesFromToken(String token) {
        return (List<String>) parseToken(token).get("roles");
    }

    /**
     * 验证 Token 是否有效
     * 检查 Token 是否过期且格式正确
     * 
     * @param token JWT Token 字符串
     * @return true 表示有效，false 表示无效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}