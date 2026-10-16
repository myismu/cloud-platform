package com.exambaby.uhub.service.code.mapstruct;

import com.exambaby.uhub.service.code.dto.RolesDTO;
import com.exambaby.uhub.service.code.entity.Roles;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * RolesMapStruct 是角色实体类（Roles）与数据传输对象（RolesDTO）之间的映射器接口。
 * <p>
 * 此接口使用 MapStruct 自动生成实现类，支持 Entity 与 DTO 之间的双向转换。
 * 用于解耦数据库模型与前端交互模型，增强代码的可维护性与安全性。
 * <p>
 * - Entity（Roles）用于数据库交互，包含所有角色属性。
 * - DTO（RolesDTO）用于前端交互，仅暴露必要字段。
 * <p>
 * 使用方式：
 * - 在需要转换的类中注入 RolesMapStruct，然后调用 toDTO / toEntity 方法即可。
 */
@Mapper(componentModel = "spring")
public interface RolesMapStruct {

    /**
     * MapStruct 自动生成的实例，可以通过 RolesMapStruct.INSTANCE 方式使用（非 Spring 管理时用）。
     */
//    RolesMapStruct INSTANCE = Mappers.getMapper(RolesMapStruct.class);

    /**
     * 将 Roles 实体类转换为 RolesDTO，用于向前端返回角色信息。
     *
     * @param roles 数据库中的角色实体对象
     * @return 转换后的 DTO 对象，仅包含必要信息
     */
    RolesDTO toDTO(Roles roles);

    /**
     * 将 RolesDTO 转换为 Roles 实体类，用于持久化操作。
     *
     * @param dto 前端传来的角色数据
     * @return 转换后的实体类，用于写入数据库
     */
    Roles toEntity(RolesDTO dto);

    /**
     * 将实体类列表转换为DTO列表
     * @param rolesList 实体类列表
     * @return DTO列表
     */
    List<RolesDTO> toDTOList(List<Roles> rolesList);

    /**
     * 将DTO列表转换为实体类列表
     * @param rolesDTOList DTO列表
     * @return 实体类列表
     */
    List<Roles> toEntityList(List<RolesDTO> rolesDTOList);
}
