package com.exambaby.uhub.service.code.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统实体类
 */
@Data
@TableName("system_info")
public class SystemInfo {
    /**
     * 系统ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 系统编码
     */
    private String code;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统描述
     */
    private String description;

    /**
     * 状态：1启用 0禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
} 