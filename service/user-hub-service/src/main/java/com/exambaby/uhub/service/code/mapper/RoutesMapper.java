package com.exambaby.uhub.service.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exambaby.uhub.service.code.entity.Routes;
import org.apache.ibatis.annotations.Mapper;

/**
 * 路由Mapper接口，继承MyBatis-Plus的BaseMapper
 * 提供基础的CRUD操作
 */
@Mapper
public interface RoutesMapper extends BaseMapper<Routes> {
} 