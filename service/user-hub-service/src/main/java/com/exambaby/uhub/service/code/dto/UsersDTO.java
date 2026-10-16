package com.exambaby.uhub.service.code.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 用户DTO类，用于存储用户的基本信息和认证信息。
 * 每个用户都有唯一的用户名和密码，用于登录系统。
 */
@Data
public class UsersDTO {

    /**
     * 主键ID，唯一标识一个用户。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名，用户登录时使用的唯一标识。
     */
    private String username;

    /**
     * 密码，用户登录时使用的密码（通常存储加密后的值）。
     */
    private String password;

    /**
     * 用户的电子邮件地址，用于接收通知或重置密码。
     */
    private String email;

    /**
     * 用户的电话号码，用于联系用户或身份验证。
     */
    private String phoneNumber;

    /**
     * 用户的昵称，用于显示在系统中。
     */
    private String avatar;
    /**
     * 是否锁定（0-否，1-是）
     */
    private int isLocked;

    /**
     * 系统ID
     */
    @TableField(exist = false)// 不在数据库中创建此字段
    @JsonSerialize(using = ToStringSerializer.class)
    private String systemCode;
    //    设置注解，非数据库字段，用于存储验证码
    @TableField(exist = false)
    private String code;
}
