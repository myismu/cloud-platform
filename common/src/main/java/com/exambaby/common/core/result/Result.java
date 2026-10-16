package com.exambaby.common.core.result;

import lombok.Data;
import java.io.Serializable;

/**
 * 通用响应结果封装类
 * @param <T> 响应数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 响应码 */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /**
     * 成功返回
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回 (含数据)
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 错误返回
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 错误返回 (自定义状态码)
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
