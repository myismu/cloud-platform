package com.exambaby.uhub.service.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exambaby.uhub.service.code.entity.LoginHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录历史记录Mapper接口，继承自MyBatis-Plus的BaseMapper
 * 提供基础的CRUD操作
 */
@Mapper
public interface LoginHistoryMapper extends BaseMapper<LoginHistory> {
} 