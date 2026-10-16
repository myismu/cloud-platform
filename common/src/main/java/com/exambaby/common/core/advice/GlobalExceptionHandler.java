package com.exambaby.common.core.advice;

import com.exambaby.common.core.domain.ApiResponse;
import com.exambaby.common.core.domain.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

/**
 * 全局异常处理器
 * 
 * 功能说明：
 * 统一处理 Controller 层抛出的异常，返回标准化的错误响应
 * 
 * 处理的异常类型：
 * - MaxUploadSizeExceededException: 文件上传大小超限异常
 * - BusinessException: 业务异常
 * - MethodArgumentNotValidException: 参数校验异常
 * - MissingServletRequestParameterException: 缺少必填参数
 * - HttpMessageNotReadableException: 请求体解析异常
 * - HttpRequestMethodNotSupportedException: 请求方法不支持
 * - NoHandlerFoundException: 404异常
 * - Exception: 其他未知异常
 * 
 * 注意事项：
 * 此类仅适用于 Spring MVC（Servlet）环境，不适用于 WebFlux 环境
 * Gateway 使用 WebFlux，因此不会加载此类
 */
@Log4j2
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalExceptionHandler {

    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        log.warn("文件上传大小超限: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("code", 413, "message", "上传文件太大，不可超过服务器限制"));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        
        log.warn("参数校验失败: {}", errorMessage);
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(400);
        response.setMessage(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理缺少必填参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParamException(MissingServletRequestParameterException ex) {
        log.warn("缺少必填参数: {}", ex.getParameterName());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(400);
        response.setMessage("缺少必填参数: " + ex.getParameterName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理请求体解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(400);
        response.setMessage("请求格式错误，请检查JSON格式是否正确");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("不支持的请求方法: {}", ex.getMethod());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(405);
        response.setMessage("不支持的请求方法: " + ex.getMethod());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(NoHandlerFoundException ex) {
        log.warn("接口不存在: {}", ex.getRequestURL());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(404);
        response.setMessage("请求的资源不存在");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        log.error("空指针异常 - 路径: {}, 消息: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(500);
        response.setMessage("系统繁忙，请稍后重试");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理数字格式异常
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ApiResponse<Object>> handleNumberFormatException(NumberFormatException ex, HttpServletRequest request) {
        log.error("数字格式异常 - 路径: {}, 消息: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(500);
        response.setMessage("系统繁忙，请稍后重试");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理业务异常
     * 业务异常的消息可以直接返回给用户，因为这是开发者预期并设计的
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 处理所有未捕获的异常
     * 
     * 安全原则：
     * 1. 不向用户暴露技术细节
     * 2. 记录日志供开发人员排查
     * 3. 返回友好的错误提示
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(HttpServletRequest request, Exception e) {
        log.error("系统异常 - 路径: {}, 方法: {}, 类型: {}, 消息: {}", 
                request.getRequestURI(), 
                request.getMethod(),
                e.getClass().getSimpleName(),
                e.getMessage());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(500);
        response.setMessage("系统繁忙，请稍后重试");
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
