package com.exambaby.uhub.service.code.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色实体类，对应数据库中的roles表
 */
@Data
@TableName("roles")
public class Roles {
    /**
     * 主键ID，使用自增策略
     */
    @TableId(type = IdType.ASSIGN_ID)//雪花id
    private Long id;

    /**
     * 系统ID
     */
    private Long systemId;

    /**
     * 角色名称，如：admin、user等
     */
    private String roleName;

    /**
     * 角色编码（用于权限控制）
     */
    private String roleCode;

    /**
     * 角色描述，说明角色的用途和权限范围
     */
    private String description;

    /**
     * 角色状态，0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间，记录数据创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间，记录数据更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}