package com.exambaby.uhub.service.code.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.common.core.domain.BusinessException;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.dto.LoginHistoryDTO;
import com.exambaby.uhub.service.code.entity.LoginHistory;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import com.exambaby.uhub.service.code.service.SystemInfoService;
import com.exambaby.uhub.service.code.mapper.LoginHistoryMapper;
import com.exambaby.uhub.service.code.mapstruct.LoginHistoryMapStruct;
import com.exambaby.uhub.service.code.service.LoginHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {

    private final LoginHistoryMapper loginHistoryMapper;
    private final LoginHistoryMapStruct loginHistoryMapStruct;
    private final SystemInfoService systemInfoService;
    public LoginHistoryServiceImpl(LoginHistoryMapper loginHistoryMapper, LoginHistoryMapStruct loginHistoryMapStruct, SystemInfoService systemInfoService) {
        this.loginHistoryMapper = loginHistoryMapper;
        this.loginHistoryMapStruct = loginHistoryMapStruct;
        this.systemInfoService = systemInfoService;
    }

    @Override
    public String saveLoginHistory(LoginHistoryDTO loginHistoryDTO) {
        //        如果没有系统代码就代表是用户请求的操作，则获取当前线程的系统代码
        if (loginHistoryDTO.getSystemId() == null) {
            //获取系统代码
            String systemCode = ThreadLocalContext.getSystemCode();
            SystemInfo systemInfo = systemInfoService.getOne(new LambdaQueryWrapper<SystemInfo>().eq(SystemInfo::getCode, systemCode));
            loginHistoryDTO.setSystemId(systemInfo.getId());
        }


        LoginHistory loginHistory = loginHistoryMapStruct.toEntity(loginHistoryDTO);
        loginHistoryMapper.insert(loginHistory);
        return "保存登录历史记录成功";
    }

    @Override
    public List<LoginHistoryDTO> getLoginHistoryByUserId(Long userId) {
        LambdaQueryWrapper<LoginHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LoginHistory::getUserId, userId)
                .orderByDesc(LoginHistory::getLoginTime);
        List<LoginHistory> loginHistories = loginHistoryMapper.selectList(queryWrapper);
        return loginHistoryMapStruct.toDTOList(loginHistories);
    }

    @Override
    public Page<LoginHistoryDTO> getLoginHistoryByPage(Page<LoginHistoryDTO> page, Long userId, String ipAddress, String username, String clientType) {
        LambdaQueryWrapper<LoginHistory> queryWrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            queryWrapper.eq(LoginHistory::getUserId, userId);
        }
        if (ipAddress != null && !ipAddress.isEmpty()) {
            queryWrapper.like(LoginHistory::getIpAddress, ipAddress);
        }
        if (username != null && !username.isEmpty()) {
            queryWrapper.like(LoginHistory::getUsername, username);
        }
        if (clientType != null && !clientType.isEmpty()) {
            queryWrapper.eq(LoginHistory::getClientType, clientType);
        }
        
        queryWrapper.orderByDesc(LoginHistory::getLoginTime);
        
        Page<LoginHistory> loginHistoryPage = loginHistoryMapper.selectPage(
                new Page<>(page.getCurrent(), page.getSize()), 
                queryWrapper
        );
        
        Page<LoginHistoryDTO> dtoPage = new Page<>(loginHistoryPage.getCurrent(), loginHistoryPage.getSize(), loginHistoryPage.getTotal());
        dtoPage.setRecords(loginHistoryMapStruct.toDTOList(loginHistoryPage.getRecords()));
        
        return dtoPage;
    }

    @Override
    public String deleteLoginHistory(Long id) {
        int result = loginHistoryMapper.deleteById(id);
        if (result > 0) {
            return "删除登录历史记录成功";
        } else {
            throw new BusinessException("删除登录历史记录失败", 444);
        }
    }
} 