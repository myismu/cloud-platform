package com.exambaby.uhub.service.code.mapstruct;



import com.exambaby.uhub.service.code.dto.RoleRoutesDTO;
import com.exambaby.uhub.service.code.entity.RoleRoutes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * RoleRoutesMapStruct 是 RoleRoutes 实体类与 RoleRoutesDTO 之间的映射器接口。
 *
 * 用于将实体对象与前端数据传输对象（DTO）进行转换。
 * 由于字段名和类型一致，MapStruct 会自动完成映射。
 */
@Mapper(componentModel = "spring")
public interface RoleRoutesMapStruct {

    RoleRoutesMapStruct INSTANCE = Mappers.getMapper(RoleRoutesMapStruct.class);

    /**
     * 将 RoleRoutes 实体类转换为 DTO。
     *
     * @param roleRoutes 实体类对象
     * @return DTO 对象
     */
    RoleRoutesDTO toDTO(RoleRoutes roleRoutes);

    /**
     * 将 RoleRoutesDTO 转换为实体类对象。
     *
     * @param dto DTO 对象
     * @return 实体类对象
     */
    RoleRoutes toEntity(RoleRoutesDTO dto);
}
