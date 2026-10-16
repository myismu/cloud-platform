package com.exambaby.common.core.advice;

import com.exambaby.common.core.domain.ApiResponse;
import com.exambaby.common.core.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应结果封装处理器
 * 
 * 功能说明：
 * 自动将 Controller 返回的结果封装为统一的 ApiResponse 格式
 * 
 * 封装规则：
 * 1. Swagger 文档接口 (/v3/*) 不进行封装
 * 2. 内部接口 (/internal/*) 不进行封装
 * 3. 已经是 ApiResponse 或 ResponseEntity 类型的不进行封装
 * 4. String 类型需要特殊处理，手动转换为 JSON 字符串
 * 5. 其他类型自动封装为 ApiResponse.success(data)
 * 
 * 注意事项：
 * 此类仅适用于 Spring MVC（Servlet）环境，不适用于 WebFlux 环境
 * Gateway 使用 WebFlux，因此不会加载此类
 */
@Log4j2
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否支持封装
     * 
     * @param returnType    返回类型
     * @param converterType 消息转换器类型
     * @return true 表示支持封装
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 在响应体写入之前进行封装处理
     * 
     * @param body                  原始响应体
     * @param returnType            返回类型
     * @param selectedContentType   内容类型
     * @param selectedConverterType 消息转换器类型
     * @param request               请求对象
     * @param response              响应对象
     * @return 封装后的响应体
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                   MediaType selectedContentType,
                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   ServerHttpRequest request,
                                   ServerHttpResponse response) {

        String path = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getRequestURI();
        
        if (path.startsWith("/v3/")) {
            return body;
        }
        
        if (path.startsWith("/internal/")) {
            return body;
        }
        
        if (body instanceof ApiResponse || body instanceof Result || body instanceof ResponseEntity) {
            return body;
        }

        if (body instanceof String) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String s = mapper.writeValueAsString(ApiResponse.success(body));
                log.info("返回结果：{}", s);
                return s;
            } catch (JsonProcessingException e) {
                return ApiResponse.error(500, "String 转 JSON 异常");
            }
        }

        return ApiResponse.success(body);
    }
}
