package com.exambaby.uhub.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户中心服务启动类
 * 
 * 功能说明：
 * 用户管理微服务，提供用户认证、授权、信息管理等核心功能
 * 
 * 主要功能：
 * - 用户注册、登录
 * - JWT Token 生成与验证
 * - 用户信息管理
 * - 在线用户管理
 * - 权限控制
 * 
 * 注解说明：
 * @MapperScan - 扫描 MyBatis Mapper 接口
 * @SpringBootApplication - Spring Boot 启动类注解
 * @EnableDiscoveryClient - 启用 Nacos 服务发现
 * @EnableFeignClients - 启用 OpenFeign 服务调用
 */
@MapperScan("com.exambaby.uhub.service.*.mapper")
@SpringBootApplication(scanBasePackages = {"com.exambaby.uhub.service", "com.exambaby.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class UserHubApplication {
    
    /**
     * 应用程序入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UserHubApplication.class, args);

    }

}
