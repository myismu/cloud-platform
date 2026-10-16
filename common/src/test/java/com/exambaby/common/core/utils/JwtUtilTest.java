package com.exambaby.common.core.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JwtUtil 单元测试
 *
 * ============================================================
 * 测试方式说明：
 * 不启动 Spring 容器，直接 new 出 JwtUtil，再用 ReflectionTestUtils
 * 手动注入 @Value 字段（jwt.secret / jwt.expiration）。
 * 好处：毫秒级完成，不依赖 Nacos、数据库等任何中间件。
 *
 * 覆盖场景：
 * 1. 签发 -> 解析：用户ID / 角色 / 系统代码能正确取回
 * 2. 有效期内的 token 校验通过
 * 3. 已过期的 token 校验不通过
 * 4. 被篡改的 token 校验不通过
 * 5. 用其他密钥签发的 token 校验不通过（防伪造）
 * 6. 非法字符串不抛异常、直接判为无效
 * ============================================================
 */
class JwtUtilTest {

    /** 测试密钥：HS256 要求至少 256 bit（32 字节），长度不能太短 */
    private static final String SECRET = "unit-test-secret-0123456789-0123456789-abcdef";

    /** 默认有效期：1 小时 */
    private static final long EXPIRATION = 60 * 60 * 1000L;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "EXPIRATION_TIME", EXPIRATION);
    }

    @Test
    @DisplayName("签发后的 token 能解析出用户ID、角色和系统代码")
    void generateAndParseToken() {
        String token = jwtUtil.generateToken("1001", List.of("admin", "user"), "ai-ppt");

        Claims claims = jwtUtil.parseToken(token);
        assertThat(claims.getSubject()).isEqualTo("1001");
        assertThat(jwtUtil.getRolesFromToken(token)).containsExactly("admin", "user");
        assertThat(claims.get("systemCode", String.class)).isEqualTo("ai-ppt");
    }

    @Test
    @DisplayName("有效期内的 token 校验通过")
    void validateValidToken() {
        String token = jwtUtil.generateToken("1001", List.of("user"), "ai-ppt");

        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("已过期的 token 校验不通过")
    void validateExpiredToken() {
        // 把有效期改成负数：签发出来的 token 生下来就是过期的
        ReflectionTestUtils.setField(jwtUtil, "EXPIRATION_TIME", -1000L);
        String token = jwtUtil.generateToken("1001", List.of("user"), "ai-ppt");

        assertThat(jwtUtil.validateToken(token)).isFalse();
    }

    @Test
    @DisplayName("被篡改的 token 校验不通过")
    void validateTamperedToken() {
        String token = jwtUtil.generateToken("1001", List.of("user"), "ai-ppt");
        // 只改最后一个字符，签名就对不上了
        String tampered = token.substring(0, token.length() - 1) + (token.endsWith("A") ? "B" : "A");

        assertThat(jwtUtil.validateToken(tampered)).isFalse();
    }

    @Test
    @DisplayName("用其他密钥签发的 token 校验不通过")
    void validateTokenSignedByAnotherKey() {
        JwtUtil other = new JwtUtil();
        ReflectionTestUtils.setField(other, "SECRET_KEY", "another-secret-9876543210-9876543210-abcdef");
        ReflectionTestUtils.setField(other, "EXPIRATION_TIME", EXPIRATION);
        String forgedToken = other.generateToken("1001", List.of("admin"), "ai-ppt");

        // 用别的密钥伪造的 token，在正确的密钥下必须被拒绝
        assertThat(jwtUtil.validateToken(forgedToken)).isFalse();
    }

    @Test
    @DisplayName("非法字符串直接判为无效，且不抛异常")
    void validateGarbageToken() {
        assertThat(jwtUtil.validateToken("not-a-jwt")).isFalse();
        assertThat(jwtUtil.validateToken("")).isFalse();
    }
}
