package com.exambaby.uhub.service.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exambaby.uhub.service.code.entity.PasswordHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 密码历史记录Mapper接口，继承自MyBatis-Plus的BaseMapper
 * 提供基础的CRUD操作
 */
public interface PasswordHistoryMapper extends BaseMapper<PasswordHistory> {
} 