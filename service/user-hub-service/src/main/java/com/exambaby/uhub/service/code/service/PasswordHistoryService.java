package com.exambaby.uhub.service.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.PasswordHistoryDTO;

import java.util.List;

public interface PasswordHistoryService {
    /**
     * 保存密码历史记录
     * @param passwordHistoryDTO 密码历史记录信息
     * @return 保存结果提示
     */
    String savePasswordHistory(PasswordHistoryDTO passwordHistoryDTO);

    /**
     * 根据用户ID获取密码历史记录
     * @param userId 用户ID
     * @return 密码历史记录列表
     */
    List<PasswordHistoryDTO> getPasswordHistoryByUserId(Long userId);

    /**
     * 分页查询密码历史记录
     * @param page 分页参数
     * @param userId 用户ID（可选）
     * @param changeReason 修改原因（可选，模糊查询）
     * @return 分页后的密码历史记录列表
     */
    Page<PasswordHistoryDTO> getPasswordHistoryByPage(Page<PasswordHistoryDTO> page, Long userId, String changeReason);

    /**
     * 删除密码历史记录
     * @param id 记录ID
     * @return 删除结果提示
     */
    String deletePasswordHistory(Long id);

    /**
     * 检查密码是否在历史记录中
     * @param userId 用户ID
     * @param password 待检查的密码
     * @return 如果密码在历史记录中返回true，否则返回false
     */
    boolean isPasswordInHistory(Long userId, String password);
} 