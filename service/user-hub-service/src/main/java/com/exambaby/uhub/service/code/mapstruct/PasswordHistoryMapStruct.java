package com.exambaby.uhub.service.code.mapstruct;

import com.exambaby.uhub.service.code.dto.PasswordHistoryDTO;
import com.exambaby.uhub.service.code.entity.PasswordHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * PasswordHistoryMapStruct 是密码历史记录实体类（PasswordHistory）与数据传输对象（PasswordHistoryDTO）之间的映射器接口。
 * <p>
 * 此接口使用 MapStruct 自动生成实现类，支持 Entity 与 DTO 之间的双向转换。
 * 用于解耦数据库模型与前端交互模型，增强代码的可维护性与安全性。
 * <p>
 * - Entity（PasswordHistory）用于数据库交互，包含所有密码历史记录属性。
 * - DTO（PasswordHistoryDTO）用于前端交互，仅暴露必要字段。
 * <p>
 * 使用方式：
 * - 在需要转换的类中注入 PasswordHistoryMapStruct，然后调用 toDTO / toEntity 方法即可。
 */
@Mapper(componentModel = "spring")
public interface PasswordHistoryMapStruct {

    /**
     * MapStruct 自动生成的实例，可以通过 PasswordHistoryMapStruct.INSTANCE 方式使用（非 Spring 管理时用）。
     */
    PasswordHistoryMapStruct INSTANCE = Mappers.getMapper(PasswordHistoryMapStruct.class);

    /**
     * 将 PasswordHistory 实体类转换为 PasswordHistoryDTO，用于向前端返回密码历史记录信息。
     *
     * @param passwordHistory 数据库中的密码历史记录实体对象
     * @return 转换后的 DTO 对象，仅包含必要信息
     */
    PasswordHistoryDTO toDTO(PasswordHistory passwordHistory);

    /**
     * 将 PasswordHistoryDTO 转换为 PasswordHistory 实体类，用于持久化操作。
     *
     * @param dto 前端传来的密码历史记录数据
     * @return 转换后的实体类，用于写入数据库
     */
    PasswordHistory toEntity(PasswordHistoryDTO dto);

    /**
     * 将实体类列表转换为DTO列表
     * @param passwordHistories 实体类列表
     * @return DTO列表
     */
    List<PasswordHistoryDTO> toDTOList(List<PasswordHistory> passwordHistories);

    /**
     * 将DTO列表转换为实体类列表
     * @param passwordHistoryDTOs DTO列表
     * @return 实体类列表
     */
    List<PasswordHistory> toEntityList(List<PasswordHistoryDTO> passwordHistoryDTOs);
}
