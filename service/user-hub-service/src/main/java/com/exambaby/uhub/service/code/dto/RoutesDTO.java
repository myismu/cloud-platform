package com.exambaby.uhub.service.code.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * 路由数据传输对象，用于前端交互
 */
@Data
public class RoutesDTO {
    /**
     * 路由ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 系统ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 父级路由ID
     */
    private Long parentId;

    /**
     * 路由图标
     */
    private String icon;

    /**
     * 路由排序
     */
    private Integer orderNum;

    /**
     * 是否隐藏（0-否，1-是）
     */
    private Boolean isHidden;

    /**
     * 路由状态
     */
    private Integer status;

    /**
     * 路由描述
     */
    private String description;

    /**
     * 子路由列表
     */
    private List<RoutesDTO> children;
}