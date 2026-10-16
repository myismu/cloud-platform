package com.exambaby.uhub.service.code.controller;

import com.exambaby.common.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 模拟用户注册消息发送
 */
@Slf4j
@RestController
@RequestMapping("/user/mq")
public class UserRegisterTestController {

    @Autowired
    private StreamBridge streamBridge;

    @GetMapping("/register")
    public String register(@RequestParam String username) {
        // 模拟业务逻辑：构造注册事件
        UserRegisterEvent event = new UserRegisterEvent(
                System.currentTimeMillis(), 
                username, 
                username + "@test.com", 
                LocalDateTime.now()
        );

        log.info("【用户服务】准备发送注册成功消息: {}", event);

        // 发送消息，这里的 "user-out-0" 必须对应 YAML 中的配置
        boolean isSuccess = streamBridge.send("user-out-0", event);

        return isSuccess ? "注册消息已发送: " + username : "消息发送失败";
    }
}
