package com.exambaby.common.core.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一用户 DTO
 * 
 * 功能说明：
 * 用于在服务间传递用户信息的数据传输对象
 * 
 * 使用场景：
 * - 服务间用户信息传递
 * - 用户登录响应
 * - 用户信息查询结果
 */
@Data
public class UsersDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     * 使用 ToStringSerializer 避免前端精度丢失
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码（加密后）
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phoneNumber;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 是否锁定（0-正常，1-锁定）
     */
    private int isLocked;

    /**
     * 系统代码（非数据库字段）
     */
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private String systemCode;

    /**
     * 验证码（非数据库字段）
     */
    @TableField(exist = false)
    private String code;
}
