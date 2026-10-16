package com.exambaby.uhub.service.code.entity;
import com.baomidou.mybatisplus.annotation.*;


import lombok.Data;

/**
 * 角色与路由关系实体类，用于表示角色与路由之间的多对多关联关系。
 * 每个角色可以关联多个路由，每个路由也可以被多个角色使用。
 */
@Data
@TableName("role_routes")
public class RoleRoutes {

    /**
     * 主键ID，唯一标识一条角色与路由的关联关系。
     */
    @TableId(type = IdType.ASSIGN_ID)//雪花id
    private Long id;

    /**
     * 系统ID
     */
    private Long systemId;

    /**
     * 角色ID，关联到具体的角色实体，表示该路由属于哪个角色。
     */
    private Long roleId;

    /**
     * 路由ID，关联到具体的路由实体，表示该角色可以访问的路由。
     */
    private Long routeId;
}