package com.exambaby.uhub.service.code.mapstruct;

import com.exambaby.uhub.service.code.dto.SystemInfoDTO;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * SystemInfoMapStruct 是系统实体类（SystemInfo）与数据传输对象（SystemInfoDTO）之间的映射器接口。
 * <p>
 * 此接口使用 MapStruct 自动生成实现类，支持 Entity 与 DTO 之间的双向转换。
 * 用于解耦数据库模型与前端交互模型，增强代码的可维护性与安全性。
 * <p>
 * - Entity（SystemInfo）用于数据库交互，包含所有系统属性。
 * - DTO（SystemInfoDTO）用于前端交互，仅暴露必要字段。
 * <p>
 * 使用方式：
 * - 在需要转换的类中注入 SystemInfoMapStruct，然后调用 toDTO / toEntity 方法即可。
 */
@Mapper(componentModel = "spring")
public interface SystemInfoMapStruct {

    /**
     * MapStruct 自动生成的实例，可以通过 SystemInfoMapStruct.INSTANCE 方式使用（非 Spring 管理时用）。
     */
    SystemInfoMapStruct INSTANCE = Mappers.getMapper(SystemInfoMapStruct.class);

    /**
     * 将 SystemInfo 实体类转换为 SystemInfoDTO，用于向前端返回系统信息。
     *
     * @param systemInfo 数据库中的系统实体对象
     * @return 转换后的 DTO 对象，仅包含必要信息
     */
    SystemInfoDTO toDTO(SystemInfo systemInfo);

    /**
     * 将 SystemInfoDTO 转换为 SystemInfo 实体类，用于持久化操作。
     *
     * @param dto 前端传来的系统数据
     * @return 转换后的实体类，用于写入数据库
     */
    SystemInfo toEntity(SystemInfoDTO dto);

    /**
     * 将实体类列表转换为DTO列表
     * @param systemInfoList 实体类列表
     * @return DTO列表
     */
    List<SystemInfoDTO> toDTOList(List<SystemInfo> systemInfoList);

    /**
     * 将DTO列表转换为实体类列表
     * @param systemInfoDTOList DTO列表
     * @return 实体类列表
     */
    List<SystemInfo> toEntityList(List<SystemInfoDTO> systemInfoDTOList);
} 