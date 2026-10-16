package com.exambaby.uhub.service.code.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisTestController {

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestController(RedisTemplate<String, Object> redisTemplateObject, StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplateObject;
        this.stringRedisTemplate = redisTemplate;
    }

    @GetMapping("/set")
    public Object set(@RequestParam String key, @RequestParam String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "设置成功";
    }

    @GetMapping("/get")
    public Object get(@RequestParam String key) {
        String s = stringRedisTemplate.opsForValue().get(key);
        return s;
    }
}
