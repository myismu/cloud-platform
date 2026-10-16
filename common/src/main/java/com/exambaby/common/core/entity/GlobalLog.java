package com.exambaby.common.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 全局日志实体类
 */
@Data
@TableName("global_log")
public class GlobalLog {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private Long systemId;   // 系统ID
    private String requestUrl;    // 请求的 URL
    private String requestMethod; // 请求方法
    private String ipAddress;     // 请求的 IP 地址
    private String requestHeaders; // 请求头信息
    private String requestParams;  // 请求参数
    private Integer responseStatusCode; // 响应状态码
    private Long responseTime;    // 响应时间（毫秒）
    private String responseHeaders; // 响应头信息
    private String responseBody;  // 响应体内容
    private String exceptionMessage; // 异常信息
    private String exceptionStackTrace; // 异常堆栈信息
    private Long userId;          // 用户 ID
    private String userName;
    private String userAgent; // 请求头中的 User-Agent

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 日志创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 日志更新时间
}
