package com.exambaby.uhub.service.code.mapstruct;

import com.exambaby.uhub.service.code.dto.UsersDTO;
import com.exambaby.uhub.service.code.entity.Users;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * UsersMapStruct 是用户实体类（Users）与数据传输对象（UsersDTO）之间的映射器接口。
 * <p>
 * 此接口使用 MapStruct 自动生成实现类，支持 Entity 与 DTO 之间的双向转换。
 * 用于解耦数据库模型与前端交互模型，增强代码的可维护性与安全性。
 * <p>
 * - Entity（Users）用于数据库交互，包含所有用户属性（如密码、加密信息等）。
 * - DTO（UsersDTO）用于前端交互，隐藏敏感信息，仅暴露必要字段。
 * <p>
 * 使用方式：
 * - 在需要转换的类中注入 UsersMapStruct，然后调用 toDTO / toEntity 方法即可。
 */
@Mapper(componentModel = "spring")
public interface UsersMapStruct {

    /**
     * MapStruct 自动生成的实例，可以通过 UsersMapStruct.INSTANCE 方式使用（非 Spring 管理时用）。
     */
//    UsersMapStruct INSTANCE = Mappers.getMapper(UsersMapStruct.class);

    /**
     * 将 Users 实体类转换为 UsersDTO，用于向前端返回用户信息。
     *
     * @param users 数据库中的用户实体对象
     * @return 转换后的 DTO 对象，仅包含非敏感信息
     */
    UsersDTO toDTO(Users users);

    /**
     * 将 UsersDTO 转换为 Users 实体类，用于持久化操作。
     *
     * @param dto 前端传来的用户数据
     * @return 转换后的实体类，用于写入数据库
     */
    Users toEntity(UsersDTO dto);

    List<UsersDTO> toDTOList(List<Users> usersList);

    List<Users> toEntityList(List<UsersDTO> usersDTOList);
}
