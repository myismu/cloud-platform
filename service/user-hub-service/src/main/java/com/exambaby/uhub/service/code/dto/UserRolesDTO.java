package com.exambaby.uhub.service.code.dto;



import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 用户角色关联DTO类，用于表示用户与角色之间的多对多关系。
 * 每个用户可以拥有多个角色，每个角色也可以被多个用户拥有。
 */
@Data
public class UserRolesDTO {
    /**
     * 主键ID，唯一标识一条用户与角色的关联关系。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 系统ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;


    /**
     * 用户ID，关联到具体的用户实体，表示该角色属于哪个用户。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 角色ID，关联到具体的角色实体，表示该用户拥有的角色。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;
}