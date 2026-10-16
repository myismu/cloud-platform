package com.exambaby.api.userhub.client;

import com.exambaby.common.core.domain.ApiResponse;
import com.exambaby.common.core.dto.UsersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户服务远程调用客户端
 * 
 * 功能说明：
 * 使用 OpenFeign 声明式调用 user-hub-service 服务
 * 
 * 特性：
 * - 通过 Nacos 服务发现自动获取服务地址
 * - 支持客户端负载均衡
 * - 集成 Sentinel 熔断降级
 * 
 * 调用的接口：
 * - 登录接口
 * - 用户信息获取接口
 * - 注册接口
 * - 个人信息修改接口
 * - 用户名查询接口
 */
@FeignClient(
    name = "user-hub-service",
    fallbackFactory = UserHubClientFallbackFactory.class
)
public interface UserHubClient {

    /**
     * 用户登录
     * 
     * @param usersDTO   登录信息（用户名、密码、系统代码）
     * @param userAgent  浏览器 User-Agent
     * @param clientIp   客户端 IP 地址
     * @return 登录响应，包含 Token
     */
    @PostMapping("/auth/login")
    ApiResponse<Map<String, Object>> login(
        @RequestBody UsersDTO usersDTO,
        @RequestHeader("User-Agent") String userAgent,
        @RequestHeader("X-Client-IP") String clientIp
    );

    /**
     * 获取当前登录用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/auth/getUserInfo")
    ApiResponse<Map<String, Object>> getUserInfo(
        @RequestHeader("X-User-Id") String userId
    );

    /**
     * 用户注册
     * 
     * @param usersDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/auth/register")
    ApiResponse<String> register(@RequestBody UsersDTO usersDTO);

    /**
     * 获取个人详细信息
     * 
     * @param userId 用户ID
     * @return 个人信息
     */
    @GetMapping("/auth/getPersonalInfo")
    ApiResponse<UsersDTO> getPersonalInfo(
        @RequestHeader("X-User-Id") String userId
    );

    /**
     * 修改个人信息
     * 
     * @param usersDTO 修改信息
     * @param userId   用户ID
     * @return 修改结果
     */
    @PostMapping("/auth/modifyPersonalInfo")
    ApiResponse<Boolean> modifyPersonalInfo(
        @RequestBody UsersDTO usersDTO,
        @RequestHeader("X-User-Id") String userId
    );

    /**
     * 根据用户ID获取用户信息
     * 
     * @param id     用户ID
     * @param userId 当前登录用户ID
     * @return 用户信息
     */
    @GetMapping("/users/{id}")
    ApiResponse<UsersDTO> getUserById(
        @PathVariable("id") Long id,
        @RequestHeader("X-User-Id") String userId
    );

    /**
     * 根据用户ID获取用户名
     * 
     * @param id 用户ID
     * @return 用户名
     */
    @GetMapping("/users/getUserNameById")
    ApiResponse<String> getUserNameById(@RequestParam("id") Long id);

    /**
     * 根据用户ID列表批量获取用户名
     *
     * @param ids 用户ID列表
     * @return userId -> username 的映射
     */
    @GetMapping("/users/getUserNamesByIds")
    ApiResponse<Map<Long, String>> getUserNamesByIds(@RequestParam("ids") List<Long> ids);
}
