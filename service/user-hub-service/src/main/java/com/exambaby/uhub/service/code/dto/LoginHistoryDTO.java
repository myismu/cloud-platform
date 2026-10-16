package com.exambaby.uhub.service.code.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录历史数据传输对象
 */
@Data
public class LoginHistoryDTO {
    /**
     * 登录历史ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 系统ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 用户名，记录用户登录时的用户名，有助于安全审计等
     */
    private String username;// 用户名

    /**
     * 登录时的IP地址，用于记录用户登录时的网络位置信息，有助于安全审计等
     */
    private String ipAddress;

    /**
     * 用户代理信息，记录用户登录时使用的客户端信息（如浏览器类型、版本等）
     */
    private String userAgent;
    /**
     * 登录设备类型，如：Android、iOS、PC、H5等
     */
    private String clientType; // 登录设备类型，如：Android、iOS、PC、H5等
}