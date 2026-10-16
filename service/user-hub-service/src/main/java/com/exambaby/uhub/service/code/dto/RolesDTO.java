package com.exambaby.uhub.service.code.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 角色数据传输对象，用于前端交互
 */
@Data
public class RolesDTO {
    /**
     * 角色ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 系统ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码（用于权限控制）
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色状态
     */
    private Integer status;
}