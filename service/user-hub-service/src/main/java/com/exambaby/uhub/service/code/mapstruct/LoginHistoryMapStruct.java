package com.exambaby.uhub.service.code.mapstruct;

import com.exambaby.uhub.service.code.dto.LoginHistoryDTO;
import com.exambaby.uhub.service.code.entity.LoginHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * LoginHistoryMapStruct 是登录历史实体类（LoginHistory）与数据传输对象（LoginHistoryDTO）之间的映射器接口。
 * <p>
 * 此接口使用 MapStruct 自动生成实现类，支持 Entity 与 DTO 之间的双向转换。
 * 用于解耦数据库模型与前端交互模型，增强代码的可维护性与安全性。
 * <p>
 * - Entity（LoginHistory）用于数据库交互，包含所有登录历史属性。
 * - DTO（LoginHistoryDTO）用于前端交互，仅暴露必要字段。
 * <p>
 * 使用方式：
 * - 在需要转换的类中注入 LoginHistoryMapStruct，然后调用 toDTO / toEntity 方法即可。
 */
@Mapper(componentModel = "spring")
public interface LoginHistoryMapStruct {

    /**
     * MapStruct 自动生成的实例，可以通过 LoginHistoryMapStruct.INSTANCE 方式使用（非 Spring 管理时用）。
     */
    LoginHistoryMapStruct INSTANCE = Mappers.getMapper(LoginHistoryMapStruct.class);

    /**
     * 将 LoginHistory 实体类转换为 LoginHistoryDTO，用于向前端返回登录历史信息。
     *
     * @param loginHistory 数据库中的登录历史实体对象
     * @return 转换后的 DTO 对象，仅包含必要信息
     */
    LoginHistoryDTO toDTO(LoginHistory loginHistory);

    /**
     * 将 LoginHistoryDTO 转换为 LoginHistory 实体类，用于持久化操作。
     *
     * @param dto 前端传来的登录历史数据
     * @return 转换后的实体类，用于写入数据库
     */
    LoginHistory toEntity(LoginHistoryDTO dto);

    /**
     * 将实体类列表转换为DTO列表
     * @param loginHistoryList 实体类列表
     * @return DTO列表
     */
    List<LoginHistoryDTO> toDTOList(List<LoginHistory> loginHistoryList);

    /**
     * 将DTO列表转换为实体类列表
     * @param loginHistoryDTOList DTO列表
     * @return 实体类列表
     */
    List<LoginHistory> toEntityList(List<LoginHistoryDTO> loginHistoryDTOList);
}