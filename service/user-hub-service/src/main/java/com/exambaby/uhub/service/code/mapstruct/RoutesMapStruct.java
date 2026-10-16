package com.exambaby.uhub.service.code.mapstruct;

import com.exambaby.uhub.service.code.dto.RoutesDTO;
import com.exambaby.uhub.service.code.entity.Routes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * RoutesMapStruct 是路由实体类（Routes）与数据传输对象（RoutesDTO）之间的映射器接口。
 * <p>
 * 此接口使用 MapStruct 自动生成实现类，支持 Entity 与 DTO 之间的双向转换。
 * 用于解耦数据库模型与前端交互模型，增强代码的可维护性与安全性。
 * <p>
 * - Entity（Routes）用于数据库交互，包含所有路由属性。
 * - DTO（RoutesDTO）用于前端交互，仅暴露必要字段。
 * <p>
 * 使用方式：
 * - 在需要转换的类中注入 RoutesMapStruct，然后调用 toDTO / toEntity 方法即可。
 */
@Mapper(componentModel = "spring")
public interface RoutesMapStruct {

    /**
     * MapStruct 自动生成的实例，可以通过 RoutesMapStruct.INSTANCE 方式使用（非 Spring 管理时用）。
     */
    RoutesMapStruct INSTANCE = Mappers.getMapper(RoutesMapStruct.class);

    /**
     * 将 Routes 实体类转换为 RoutesDTO，用于向前端返回路由信息。
     *
     * @param routes 数据库中的路由实体对象
     * @return 转换后的 DTO 对象，仅包含必要信息
     */
    RoutesDTO toDTO(Routes routes);

    /**
     * 将 RoutesDTO 转换为 Routes 实体类，用于持久化操作。
     *
     * @param dto 前端传来的路由数据
     * @return 转换后的实体类，用于写入数据库
     */
    Routes toEntity(RoutesDTO dto);

    /**
     * 将实体类列表转换为DTO列表
     * @param routesList 实体类列表
     * @return DTO列表
     */
    List<RoutesDTO> toDTOList(List<Routes> routesList);

    /**
     * 将DTO列表转换为实体类列表
     * @param routesDTOList DTO列表
     * @return 实体类列表
     */
    List<Routes> toEntityList(List<RoutesDTO> routesDTOList);
}
