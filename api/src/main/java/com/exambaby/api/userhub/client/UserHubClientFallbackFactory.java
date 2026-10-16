package com.exambaby.api.userhub.client;

import com.exambaby.common.core.domain.ApiResponse;
import com.exambaby.common.core.dto.UsersDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * UserHubClient 降级工厂类
 * 
 * 功能说明：
 * 为 UserHubClient 提供统一的熔断降级处理
 * 
 * 工作原理：
 * 当 user-hub-service 服务不可用时（如服务宕机、网络超时、触发熔断规则等），
 * OpenFeign 会自动调用此工厂创建的降级实现，返回友好的错误提示
 * 
 * 集成 Sentinel：
 * 需要在配置中开启 feign.sentinel.enabled=true
 * 
 * 降级场景：
 * - 服务提供者宕机
 * - 网络连接超时
 * - 触发 Sentinel 熔断规则
 * - 服务端返回 5xx 错误
 */
@Component
public class UserHubClientFallbackFactory implements FallbackFactory<UserHubClient> {

    @Override
    public UserHubClient create(Throwable cause) {
        return new UserHubClient() {
            
            /**
             * 登录接口降级处理
             */
            @Override
            public ApiResponse<Map<String, Object>> login(UsersDTO usersDTO, String userAgent, String clientIp) {
                return ApiResponse.error(503, "用户服务暂时不可用，请稍后再试");
            }

            /**
             * 获取用户信息降级处理
             */
            @Override
            public ApiResponse<Map<String, Object>> getUserInfo(String userId) {
                return ApiResponse.error(503, "用户服务暂时不可用，请稍后再试");
            }

            /**
             * 注册接口降级处理
             */
            @Override
            public ApiResponse<String> register(UsersDTO usersDTO) {
                return ApiResponse.error(503, "用户服务暂时不可用，请稍后再试");
            }

            /**
             * 获取个人信息降级处理
             */
            @Override
            public ApiResponse<UsersDTO> getPersonalInfo(String userId) {
                return ApiResponse.error(503, "用户服务暂时不可用，请稍后再试");
            }

            /**
             * 修改个人信息降级处理
             */
            @Override
            public ApiResponse<Boolean> modifyPersonalInfo(UsersDTO usersDTO, String userId) {
                return ApiResponse.error(503, "用户服务暂时不可用，请稍后再试");
            }

            /**
             * 根据ID获取用户降级处理
             */
            @Override
            public ApiResponse<UsersDTO> getUserById(Long id, String userId) {
                return ApiResponse.error(503, "用户服务暂时不可用，请稍后再试");
            }

            /**
             * 获取用户名降级处理
             */
            @Override
            public ApiResponse<String> getUserNameById(Long id) {
                return ApiResponse.error(503, "用户服务暂时不可用，请稍后再试");
            }

            @Override
            public ApiResponse<Map<Long, String>> getUserNamesByIds(List<Long> ids) {
                return new ApiResponse<>(200, "用户服务暂时不可用", Collections.emptyMap());
            }
        };
    }
}
