package com.exambaby.uhub.service.code.mapstruct;

import com.exambaby.uhub.service.code.dto.UserRolesDTO;
import com.exambaby.uhub.service.code.entity.UserRoles;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * UserRoles 与 UserRolesDTO 之间的映射接口。
 * 用于在用户角色关联的实体类和传输对象之间进行转换。
 */
@Mapper
public interface UserRolesMapStruct {

    UserRolesMapStruct INSTANCE = Mappers.getMapper(UserRolesMapStruct.class);

    /**
     * 将 UserRoles 实体转换为 UserRolesDTO。
     *
     * @param userRoles 用户与角色的实体
     * @return 转换后的 UserRolesDTO
     */
    UserRolesDTO toDTO(UserRoles userRoles);

    /**
     * 将 UserRolesDTO 转换为 UserRoles 实体。
     *
     * @param userRolesDTO 用户与角色的传输对象
     * @return 转换后的 UserRoles 实体
     */
    UserRoles toEntity(UserRolesDTO userRolesDTO);

    /**
     * 将实体列表转换为DTO列表。
     *
     * @param userRolesList 实体列表
     * @return DTO列表
     */
    List<UserRolesDTO> toDTOList(List<UserRoles> userRolesList);

    /**
     * 将DTO列表转换为实体列表。
     *
     * @param userRolesDTOList DTO列表
     * @return 实体列表
     */
    List<UserRoles> toEntityList(List<UserRolesDTO> userRolesDTOList);
}
