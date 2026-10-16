package com.exambaby.uhub.service.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录历史记录实体类，对应数据库中的login_history表
 */
@Data
@TableName("login_history")
public class LoginHistory {
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
     * 用户ID，关联到users表
     */
    private Long userId;
    private String username;// 用户名
    /**
     * 登录时间，记录用户登录的具体时间点
     */
    private LocalDateTime loginTime;

    /**
     * 登录IP地址，记录用户登录时的网络位置
     */
    private String ipAddress;

    /**
     * 用户代理信息，记录用户登录时使用的客户端信息
     */
    private String userAgent;
    private String clientType; // 登录设备类型，如：Android、iOS、PC、H5等
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