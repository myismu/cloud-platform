package com.exambaby.uhub.service.code.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exambaby.uhub.service.code.entity.Users;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    // 用 MyBatis-Plus，基本的增删查改已经都不用写了
}
