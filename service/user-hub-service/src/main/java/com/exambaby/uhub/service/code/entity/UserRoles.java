package com.exambaby.uhub.service.code.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户角色关联实体类，用于表示用户与角色之间的多对多关系。
 * 每个用户可以拥有多个角色，每个角色也可以被多个用户拥有。
 */
@Data
@TableName("user_roles")
public class UserRoles {

    /**
     * 主键ID，唯一标识一条用户与角色的关联关系。
     */
    @TableId(type = IdType.ASSIGN_ID)//雪花id
    private Long id;

    /**
     * 系统ID
     */
    private Long systemId;

    /**
     * 用户ID，关联到具体的用户实体，表示该角色属于哪个用户。
     */
    private Long userId;

    /**
     * 角色ID，关联到具体的角色实体，表示该用户拥有的角色。
     */
    private Long roleId;
}