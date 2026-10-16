package com.exambaby.uhub.service.code.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 密码历史记录实体类，对应数据库中的password_history表
 */
@Data
@TableName("password_history")
public class PasswordHistory {
    /**
     * 主键ID，使用自增策略
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 系统ID
     */
    private Long systemId;

    /**
     * 用户ID，关联到users表
     */
    private Long userId;

    /**
     * 历史密码，存储加密后的密码
     */
    private String password;

    /**
     * 修改时间，记录密码修改的具体时间点
     * 插入时自动填充当前时间
     */
    private LocalDateTime changeTime;

    /**
     * 修改原因，记录密码修改的原因（如：定期更换、忘记密码等）
     */
    private String changeReason;

    /**
     * 创建时间，记录数据创建时间
     * 插入时自动填充当前时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间，记录数据更新时间
     * 插入和更新时自动填充当前时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}