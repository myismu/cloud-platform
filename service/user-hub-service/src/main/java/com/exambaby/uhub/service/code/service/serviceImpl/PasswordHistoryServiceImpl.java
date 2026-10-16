package com.exambaby.uhub.service.code.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.common.core.domain.BusinessException;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.dto.PasswordHistoryDTO;
import com.exambaby.uhub.service.code.entity.PasswordHistory;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import com.exambaby.uhub.service.code.mapper.PasswordHistoryMapper;
import com.exambaby.uhub.service.code.mapstruct.PasswordHistoryMapStruct;
import com.exambaby.uhub.service.code.service.PasswordHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PasswordHistoryServiceImpl implements PasswordHistoryService {

    private final PasswordHistoryMapper passwordHistoryMapper;
    private final PasswordHistoryMapStruct passwordHistoryMapStruct;
    private final SystemInfoServiceImpl systemInfoService;
    public PasswordHistoryServiceImpl(PasswordHistoryMapper passwordHistoryMapper, PasswordHistoryMapStruct passwordHistoryMapStruct, SystemInfoServiceImpl systemInfoService) {
        this.passwordHistoryMapper = passwordHistoryMapper;
        this.passwordHistoryMapStruct = passwordHistoryMapStruct;
        this.systemInfoService = systemInfoService;
    }

    /**
     * 保存密码历史记录
     * @param passwordHistoryDTO 密码历史记录信息
     * @return "保存密码历史记录成功"
     */
    @Override
    public String savePasswordHistory(PasswordHistoryDTO passwordHistoryDTO) {
        //如果没有系统代码就代表是用户请求的操作，则获取当前线程的系统代码
        if (passwordHistoryDTO.getSystemId() == null) {
            //获取系统代码
            String systemCode = ThreadLocalContext.getSystemCode();
            SystemInfo systemInfo = systemInfoService.getOne(new LambdaQueryWrapper<SystemInfo>().eq(SystemInfo::getCode, systemCode));
            passwordHistoryDTO.setSystemId(systemInfo.getId());
        }

        PasswordHistory passwordHistory = passwordHistoryMapStruct.toEntity(passwordHistoryDTO);
        passwordHistoryMapper.insert(passwordHistory);
        return "保存密码历史记录成功";
    }

    /**
     * 获取用户的密码历史记录
     * @param userId 用户ID
     * @return 密码历史记录列表
     */
    @Override
    public List<PasswordHistoryDTO> getPasswordHistoryByUserId(Long userId) {
        LambdaQueryWrapper<PasswordHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PasswordHistory::getUserId, userId)
                .orderByDesc(PasswordHistory::getChangeTime);
        List<PasswordHistory> passwordHistories = passwordHistoryMapper.selectList(queryWrapper);
        return passwordHistoryMapStruct.toDTOList(passwordHistories);
    }

    /**
     * 分页获取密码历史记录
     * @param page 分页参数
     * @param userId 用户ID（可选）
     * @param changeReason 修改原因（可选，模糊查询）
     * @return 分页密码历史记录
     */
    @Override
    public Page<PasswordHistoryDTO> getPasswordHistoryByPage(Page<PasswordHistoryDTO> page, Long userId, String changeReason) {
        LambdaQueryWrapper<PasswordHistory> queryWrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            queryWrapper.eq(PasswordHistory::getUserId, userId);
        }
        if (StringUtils.hasText(changeReason)) {
            queryWrapper.like(PasswordHistory::getChangeReason, changeReason);
        }
        
        queryWrapper.orderByDesc(PasswordHistory::getChangeTime);
        
        Page<PasswordHistory> passwordHistoryPage = passwordHistoryMapper.selectPage(
                new Page<>(page.getCurrent(), page.getSize()), 
                queryWrapper
        );
        
        Page<PasswordHistoryDTO> dtoPage = new Page<>(passwordHistoryPage.getCurrent(), passwordHistoryPage.getSize(), passwordHistoryPage.getTotal());
        dtoPage.setRecords(passwordHistoryMapStruct.toDTOList(passwordHistoryPage.getRecords()));
        
        return dtoPage;
    }

    /**
     * 删除密码历史记录
     * @param id 记录ID
     * @return "删除密码历史记录成功"
     */
    @Override
    public String deletePasswordHistory(Long id) {
        int result = passwordHistoryMapper.deleteById(id);
        if (result > 0) {
            return "删除密码历史记录成功";
        } else {
            throw new BusinessException("删除密码历史记录失败", 444);
        }
    }

    /**
     * 检查密码是否在历史记录中
     * @param userId 用户ID
     * @param password 待检查的密码
     * @return true：密码在历史记录中；false：密码不在历史记录中
     */
    @Override
    public boolean isPasswordInHistory(Long userId, String password) {
        LambdaQueryWrapper<PasswordHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PasswordHistory::getUserId, userId)
                .eq(PasswordHistory::getPassword, password);
        return passwordHistoryMapper.selectCount(queryWrapper) > 0;
    }
} 