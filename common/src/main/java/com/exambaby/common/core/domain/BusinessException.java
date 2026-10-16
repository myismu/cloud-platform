package com.exambaby.common.core.domain;

import lombok.Getter;

/**
 * 通用业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
}
