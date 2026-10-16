package com.exambaby.uhub.service.code.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 角色与路由关系DTO类，用于表示角色与路由之间的多对多关联关系。
 * 每个角色可以关联多个路由，每个路由也可以被多个角色使用。
 */
@Data
public class RoleRoutesDTO {
    /**
     * 主键ID，唯一标识一条角色与路由的关联关系。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 系统ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;


    /**
     * 角色ID，关联到具体的角色实体，表示该路由属于哪个角色。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /**
     * 路由ID，关联到具体的路由实体，表示该角色可以访问的路由。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long routeId;
}