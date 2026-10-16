package com.exambaby.uhub.service.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exambaby.common.core.entity.GlobalLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GlobalLogMapper extends BaseMapper<GlobalLog> {
    // 这里可以定义额外的查询、更新操作方法（如果有需求的话）
}