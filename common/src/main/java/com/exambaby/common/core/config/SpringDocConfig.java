package com.exambaby.common.core.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc (OpenAPI 3.0) 配置类
 * 
 * 功能说明：
 * 配置 Swagger/OpenAPI 文档的基本信息，用于生成 API 文档
 * 
 * 访问地址：
 * - API 文档: http://localhost:port/swagger-ui.html
 * - OpenAPI JSON: http://localhost:port/v3/api-docs
 * 
 * 注意事项：
 * 此类仅在 OpenAPI 类存在时加载（即 springdoc 依赖存在时）
 * Gateway 使用 WebFlux，已排除 springdoc 依赖，因此不会加载此类
 */
@Configuration
@ConditionalOnClass(OpenAPI.class)
public class SpringDocConfig {

    /**
     * 创建 OpenAPI 文档配置
     * 
     * @return OpenAPI 文档配置实例
     */
    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("ExamBaby API")
                        .description("ExamBaby 系统接口文档")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("ExamBaby Documentation")
                        .url("https://github.com/exambaby"));
    }
}
