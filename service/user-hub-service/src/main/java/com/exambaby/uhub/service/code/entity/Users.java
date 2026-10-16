package com.exambaby.uhub.service.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 用户实体类，用于存储用户的基本信息和认证信息。
 * 每个用户都有唯一的用户名和密码，用于登录系统。
 */
@Data
@TableName("users")
public class Users {

    /**
     * 主键ID，唯一标识一个用户。
     */
    @TableId(type = IdType.ASSIGN_ID)//雪花id
    @JsonSerialize(using = ToStringSerializer.class)
//    @TableId(type = IdType.AUTO)//自增id
    private Long id;

    /**
     * 用户名，用户登录时使用的唯一标识。
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码，用户登录时使用的密码（通常存储加密后的值）。
     */
    @NotBlank(message = "密码不能为空")
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
     * 用户的头像地址，用于展示用户的个人形象。
     */
    private String avatar;
    /**
     * 是否锁定（0-否，1-是）
     */
    private int isLocked;

    /**
     * 创建时间，记录用户创建的时间点。
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间，记录用户信息最后更新的时间点。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;



}
