package com.exambaby.file.controller;

import com.exambaby.common.core.domain.ApiResponse;
import com.exambaby.common.core.domain.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 文件上传控制器
 * 支持上传到不同的子目录（img、image、projects）
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.base-dir:upload}")
    private String baseDir;

    /**
     * 上传图片（用户头像、题目图片等）
     * 保存到 upload/img/ 目录，返回相对路径 /img/xxx.jpg
     */
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        return doUpload(file, "img", "/img/");
    }

    /**
     * 上传 PPT 相关图片（AI 生成图、用户插入图等）
     * 保存到 upload/image/ 目录，返回相对路径 /image/xxx.jpg
     */
    @PostMapping("/upload/image")
    public String uploadPptImage(@RequestParam("file") MultipartFile file) {
        return doUpload(file, "image", "/image/");
    }

    /**
     * 上传 PPT 项目文本文件（JSON 内容、主题等）
     * 保存到 upload/projects/{projectId}/{fileName}
     * @param projectId 项目 ID
     * @param fileName  文件名 (如 content.json, theme.json)
     * @param body      包含 content 字段的 JSON body
     * @return 相对访问路径 /projects/{projectId}/{fileName}
     */
    @PostMapping("/upload/project-text")
    public ApiResponse<String> uploadProjectText(@RequestParam("projectId") String projectId,
                                                 @RequestParam("fileName") String fileName,
                                                 @RequestBody java.util.Map<String, String> body) {
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            return ApiResponse.error(400, "内容不能为空");
        }

        if (fileName == null || fileName.isBlank() || fileName.contains("/") || fileName.contains("..")) {
            return ApiResponse.error(400, "非法文件名");
        }

        Path dir = resolveBasePath().resolve("projects").resolve(projectId);
        Path filePath = dir.resolve(fileName);

        try {
            Files.createDirectories(dir);
            Files.writeString(filePath, content, StandardCharsets.UTF_8);
            return ApiResponse.success("/projects/" + projectId + "/" + fileName);
        } catch (IOException e) {
            return ApiResponse.error(500, "保存文件失败");
        }
    }

    /**
     * 上传 PPT 项目资源文件（图片/视频）
     * 保存到 upload/projects/{projectId}/assets/{uuid}.ext
     * @param projectId 项目 ID
     * @param file      资源文件
     * @return 相对访问路径 /projects/{projectId}/assets/{uuid}.ext
     */
    @PostMapping("/upload/project-asset")
    public ApiResponse<String> uploadProjectAsset(@RequestParam("projectId") String projectId,
                                     @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "上传文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".bin";
        String fileName = UUID.randomUUID() + extension;

        Path savePath = resolveBasePath().resolve("projects").resolve(projectId)
                .resolve("assets").resolve(fileName);

        try {
            Files.createDirectories(savePath.getParent());
            file.transferTo(savePath.toFile());
            return ApiResponse.success("/projects/" + projectId + "/assets/" + fileName);
        } catch (IOException e) {
            return ApiResponse.error(500, "上传资源文件失败");
        }
    }

    /**
     * 删除 PPT 项目的所有文件（项目删除时调用）
     * @param projectId 项目 ID
     */
    @DeleteMapping("/delete/project/{projectId}")
    public String deleteProjectFiles(@PathVariable String projectId) {
        Path projectDir = resolveBasePath().resolve("projects").resolve(projectId);
        if (!Files.exists(projectDir)) {
            return "目录不存在";
        }
        try (Stream<Path> paths = Files.walk(projectDir)) {
            paths.sorted(Comparator.reverseOrder())
                 .forEach(path -> {
                     try { Files.deleteIfExists(path); } catch (IOException ignored) {}
                 });
            return "删除成功";
        } catch (IOException e) {
            throw new BusinessException("删除项目文件失败", 500);
        }
    }

    /**
     * 删除图片
     * @param url 图片相对路径，如 /img/xxx.jpg 或 /image/xxx.jpg
     */
    @DeleteMapping("/deleteImage")
    public String deleteImage(@RequestParam("url") String url) {
        try {
            String cleanUrl = url.startsWith("/") ? url.substring(1) : url;
            Path filePath = resolveBasePath().resolve(cleanUrl);
            Files.deleteIfExists(filePath);
            return "删除成功";
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("删除失败", 500);
        }
    }

    /**
     * 通用上传逻辑
     */
    private String doUpload(MultipartFile file, String subDir, String prefix) {
        if (file.isEmpty()) {
            throw new BusinessException("上传文件为空", 400);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".bin";
        String fileName = UUID.randomUUID() + extension;

        Path savePath = resolveBasePath().resolve(subDir).resolve(fileName);

        try {
            Files.createDirectories(savePath.getParent());
            file.transferTo(savePath.toFile());
            return prefix + fileName;
        } catch (IOException e) {
            throw new BusinessException("上传文件失败", 500);
        }
    }

    /**
     * 聚合计算多个项目的文件总大小（字节）
     * 遍历 upload/projects/{projectId} 目录下所有文件，累加大小
     *
     * @param projectIds 项目 ID 列表
     * @return 文件总大小（字节数字符串）
     */
    @PostMapping("/storage/total-size")
    public ApiResponse<String> getProjectsTotalSize(@RequestBody java.util.List<String> projectIds) {
        long totalSize = 0;
        for (String projectId : projectIds) {
            Path projectDir = resolveBasePath().resolve("projects").resolve(projectId);
            if (Files.exists(projectDir) && Files.isDirectory(projectDir)) {
                try (Stream<Path> paths = Files.walk(projectDir)) {
                    totalSize += paths.filter(Files::isRegularFile)
                            .mapToLong(p -> {
                                try { return Files.size(p); } catch (IOException e) { return 0; }
                            })
                            .sum();
                } catch (IOException ignored) {}
            }
        }
        return ApiResponse.success(String.valueOf(totalSize));
    }

    /**
     * 解析 base-dir 为绝对路径
     */
    private Path resolveBasePath() {
        Path path = Paths.get(baseDir);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir"), baseDir);
        }
        return path.toAbsolutePath().normalize();
    }
}

