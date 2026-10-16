package com.exambaby.uhub.service.code.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 路由实体类，对应数据库中的routes表
 */
@Data
@TableName("routes")
public class Routes {
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
     * 路由路径，如：/api/users
     */
    private String path;

    /**
     * 路由名称，用于前端路由匹配
     */
    private String name;

    /**
     * 组件路径，前端组件的位置
     */
    private String component;

    /**
     * 父级路由ID，用于构建路由树
     */
    private Long parentId;

    /**
     * 路由图标，用于前端显示
     */
    private String icon;

    /**
     * 路由排序，用于前端显示顺序
     */
    private Integer orderNum;

    /**
     * 是否隐藏（0-否，1-是）
     */
    private Boolean isHidden;

    /**
     * 路由状态，0-禁用，1-启用
     */
    private Integer status;

    /**
     * 路由描述
     */
    private String description;

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