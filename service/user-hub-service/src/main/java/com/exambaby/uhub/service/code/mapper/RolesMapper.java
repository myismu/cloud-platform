package com.exambaby.uhub.service.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exambaby.uhub.service.code.entity.Roles;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色Mapper接口，继承MyBatis-Plus的BaseMapper
 * 提供基础的CRUD操作
 */
@Mapper
public interface RolesMapper extends BaseMapper<Roles> {
} 