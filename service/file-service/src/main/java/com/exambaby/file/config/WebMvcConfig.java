package com.exambaby.file.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web MVC 配置类
 * 统一管理所有静态资源的 URL 映射
 *
 * 资源目录结构：
 *   upload/img/       → /img/**       用户上传的题库图片等
 *   upload/image/     → /image/**     AI 生成的图片
 *   upload/templates/ → /templates/** PPT 模板 JSON 及预览图
 */
@Log4j2
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.base-dir:upload}")
    private String baseDir;

    @PostConstruct
    public void init() {
        String basePath = resolveBasePath();
        log.info("====== 静态资源基础目录: {} ======", basePath);
        log.info("  模板目录: {}", basePath + "templates" + File.separator);
        log.info("  图片目录: {}", basePath + "img" + File.separator);

        // 检查目录是否存在
        File templatesDir = new File(basePath + "templates");
        log.info("  模板目录是否存在: {}", templatesDir.exists());
        if (templatesDir.exists()) {
            String[] files = templatesDir.list();
            log.info("  模板目录文件数: {}", files != null ? files.length : 0);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String basePath = resolveBasePath();

        // 构建 file: URI（Spring 资源定位要求格式）
        // Windows: file:///E:/demo/.../upload/
        // Linux:   file:///home/.../upload/
        String baseFileUri = new File(basePath).toURI().toString();
        // 确保 URI 以 / 结尾，防止拼接子路径时出错（如 uploadtemplates/）
        if (!baseFileUri.endsWith("/")) {
            baseFileUri += "/";
        }

        log.info("====== 注册静态资源处理器 ======");
        log.info("  baseFileUri = {}", baseFileUri);

        // 用户上传图片：/img/** → upload/img/
        registry.addResourceHandler("/img/**")
                .addResourceLocations(baseFileUri + "img/");

        // AI 生成图片：/image/** → upload/image/
        registry.addResourceHandler("/image/**")
                .addResourceLocations(baseFileUri + "image/");

        // PPT 模板文件：/templates/** → upload/templates/
        String templatesLocation = baseFileUri + "templates/";
        log.info("  /templates/** → {}", templatesLocation);
        registry.addResourceHandler("/templates/**")
                .addResourceLocations(templatesLocation);

        // PPT 项目文件：/projects/** → upload/projects/
        String projectsLocation = baseFileUri + "projects/";
        log.info("  /projects/** → {}", projectsLocation);
        registry.addResourceHandler("/projects/**")
                .addResourceLocations(projectsLocation);
    }

    /**
     * 解析 base-dir 为绝对路径（末尾带分隔符）
     */
    private String resolveBasePath() {
        File file = new File(baseDir);
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("user.dir"), baseDir);
        }
        String path = file.getAbsolutePath();
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        return path;
    }
}

