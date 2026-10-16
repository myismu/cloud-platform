package com.exambaby.uhub.service.code.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exambaby.common.core.entity.GlobalLog;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import com.exambaby.uhub.service.code.mapper.GlobalLogMapper;
import com.exambaby.uhub.service.code.service.GlobalLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: 全局日志服务实现类
 */
@Service
public class GlobalLogServiceImpl implements GlobalLogService {

    private final GlobalLogMapper globalLogMapper;
    private  final SystemInfoServiceImpl systemInfoService;
    public GlobalLogServiceImpl(GlobalLogMapper globalLogMapper, SystemInfoServiceImpl systemInfoService) {
        this.globalLogMapper = globalLogMapper;
        this.systemInfoService = systemInfoService;
    }

    @Override
    public boolean saveLog(GlobalLog log) {
        log.setSystemId(ThreadLocalContext.getSystemId());
//        如果没有系统代码就代表是用户请求的操作，则获取当前线程的系统代码
        if (log.getSystemId() == null) {
            //获取系统代码
            String systemCode = ThreadLocalContext.getSystemCode();
            SystemInfo systemInfo = systemInfoService.getOne(new LambdaQueryWrapper<SystemInfo>().eq(SystemInfo::getCode, systemCode));
            if (systemInfo != null) {
                log.setSystemId(systemInfo.getId());
            }
        }
        return globalLogMapper.insert(log) > 0;
    }

    @Override
    public boolean removeLogById(Long logId) {
        return globalLogMapper.deleteById(logId) > 0;
    }

    @Override
    public boolean updateLog(GlobalLog log) {
        return globalLogMapper.updateById(log) > 0;
    }

    @Override
    public GlobalLog getLogById(Long logId) {
        return globalLogMapper.selectById(logId);
    }

    @Override
    public List<GlobalLog> getAllLogs() {
        return globalLogMapper.selectList(null);  // 查询所有记录
    }
}
