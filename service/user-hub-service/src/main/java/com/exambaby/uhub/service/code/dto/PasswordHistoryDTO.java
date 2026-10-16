package com.exambaby.uhub.service.code.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 密码历史记录DTO类，用于记录用户的密码修改历史
 */
@Data
public class PasswordHistoryDTO {
    /**
     * 主键ID，唯一标识一条密码历史记录
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 系统ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;


    /**
     * 用户ID，关联到修改密码的用户
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 历史密码，存储加密后的密码
     */
    private String password;

    /**
     * 修改时间，记录密码修改的具体时间点
     */
    private LocalDateTime changeTime;

    /**
     * 修改原因，记录密码修改的原因（如：定期更换、忘记密码等）
     */
    private String changeReason;
}