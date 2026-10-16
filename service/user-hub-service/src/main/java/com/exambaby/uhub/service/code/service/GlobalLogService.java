package com.exambaby.uhub.service.code.service;

import com.exambaby.common.core.entity.GlobalLog;

import java.util.List;


public interface GlobalLogService {
    boolean saveLog(GlobalLog log);  // 保存日志
    boolean removeLogById(Long logId);  // 根据 ID 删除日志
    boolean updateLog(GlobalLog log);  // 更新日志
    GlobalLog getLogById(Long logId);  // 根据 ID 查询日志
    List<GlobalLog> getAllLogs();  // 获取所有日志

}