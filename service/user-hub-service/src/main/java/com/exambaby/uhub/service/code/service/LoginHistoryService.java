package com.exambaby.uhub.service.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.LoginHistoryDTO;

import java.util.List;

public interface LoginHistoryService {
    /**
     * 保存登录历史记录
     * @param loginHistoryDTO 登录历史记录信息
     * @return 保存结果提示
     */
    String saveLoginHistory(LoginHistoryDTO loginHistoryDTO);

    /**
     * 根据用户ID获取登录历史记录
     * @param userId 用户ID
     * @return 登录历史记录列表
     */
    List<LoginHistoryDTO> getLoginHistoryByUserId(Long userId);

    /**
     * 分页查询登录历史记录
     * @param page 分页参数
     * @param userId 用户ID
     * @param ipAddress IP地址
     * @param username 用户名
     * @param clientType 客户端类型
     * @return 分页结果
     */
    Page<LoginHistoryDTO> getLoginHistoryByPage(Page<LoginHistoryDTO> page, Long userId, String ipAddress, String username, String clientType);

    /**
     * 删除登录历史记录
     * @param id 记录ID
     * @return 删除结果提示
     */
    String deleteLoginHistory(Long id);
} 