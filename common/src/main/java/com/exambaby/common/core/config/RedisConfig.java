package com.exambaby.common.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 
 * 功能说明：
 * 配置 RedisTemplate 的序列化方式，确保存储到 Redis 的数据格式正确
 * 
 * 序列化策略：
 * - Key: 使用 StringRedisSerializer，以字符串形式存储
 * - Value: 使用 GenericJackson2JsonRedisSerializer，以 JSON 格式存储
 * 
 * JSON 序列化特点：
 * - 存储时会携带 @class 类型信息
 * - 取出时会根据 @class 信息自动反序列化为对应类型
 */
@Configuration
public class RedisConfig {

    /**
     * 创建 RedisTemplate 实例
     * 使用 GenericJackson2JsonRedisSerializer 序列化器
     * 
     * 特点：
     * - 存储到 Redis 的数据会携带 @class 信息
     * - 取出时会根据 @class 信息自动反序列化成对象
     * 
     * @param connectionFactory Redis 连接工厂
     * @return RedisTemplate 序列化模板
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }
}
