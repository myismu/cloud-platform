package com.exambaby.api.file.client;

import com.exambaby.common.core.domain.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * file-service 契约：文件服务 Feign 客户端
 *
 * ============================================================
 * 使用说明：
 * 调用方（如 ppt-service）统一依赖本接口，不要各自手写 Feign 接口。
 * 契约由文件服务提供方维护，接口变更时随 api 库一起发布新版本，
 * 调用方升级版本时编译期即可发现不兼容。
 *
 * 注意：file-service 的 GlobalResponseHandler 会将返回值包装成
 * ApiResponse，因此返回类型为 ApiResponse<String>。
 * ============================================================
 */
@FeignClient(name = "file-service")
public interface FileServiceClient {

    /**
     * 上传 PPT 项目文本文件（JSON 内容、主题等）
     *
     * @param projectId 项目 ID
     * @param fileName  文件名
     * @param body      包含 content 字段的 Map
     * @return 文件路径（ApiResponse 包装）
     */
    @PostMapping("/file/upload/project-text")
    ApiResponse<String> uploadProjectText(@RequestParam("projectId") String projectId,
                                          @RequestParam("fileName") String fileName,
                                          @RequestBody Map<String, String> body);

    /**
     * 删除项目所有文件
     *
     * @param projectId 项目 ID
     */
    @DeleteMapping("/file/delete/project/{projectId}")
    ApiResponse<String> deleteProjectFiles(@PathVariable("projectId") String projectId);

    /**
     * 聚合计算多个项目的文件总大小（字节）
     *
     * @param projectIds 项目 ID 列表
     * @return 文件总大小（字节数的字符串）
     */
    @PostMapping("/file/storage/total-size")
    ApiResponse<String> getProjectsTotalSize(@RequestBody List<String> projectIds);
}
