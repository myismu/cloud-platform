package com.exambaby.uhub.service.code.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 系统数据传输对象
 */
@Data
public class SystemInfoDTO {
    /**
     * 系统ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
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
} 