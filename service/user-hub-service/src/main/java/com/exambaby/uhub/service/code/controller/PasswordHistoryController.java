package com.exambaby.uhub.service.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.PasswordHistoryDTO;
import com.exambaby.uhub.service.code.service.PasswordHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/password-history")
@Tag(name = "密码历史记录管理", description = "管理用户密码修改历史记录")
public class PasswordHistoryController {

    private final PasswordHistoryService passwordHistoryService;

    public PasswordHistoryController(PasswordHistoryService passwordHistoryService) {
        this.passwordHistoryService = passwordHistoryService;
    }

    @PostMapping
    @Operation(summary = "保存密码历史记录", description = "记录用户密码修改信息")
    public String savePasswordHistory(@RequestBody PasswordHistoryDTO passwordHistoryDTO) {
        return passwordHistoryService.savePasswordHistory(passwordHistoryDTO);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户密码历史", description = "根据用户ID获取密码修改历史记录")
    public List<PasswordHistoryDTO> getPasswordHistoryByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return passwordHistoryService.getPasswordHistoryByUserId(userId);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询密码历史", description = "分页查询密码修改历史记录，支持按用户ID和修改原因筛选")
    public Page<PasswordHistoryDTO> getPasswordHistoryByPage(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "修改原因") @RequestParam(required = false) String changeReason) {
        Page<PasswordHistoryDTO> page = new Page<>(current, size);
        return passwordHistoryService.getPasswordHistoryByPage(page, userId, changeReason);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除密码历史记录", description = "根据记录ID删除密码修改历史")
    public String deletePasswordHistory(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        return passwordHistoryService.deletePasswordHistory(id);
    }

    @GetMapping("/check")
    @Operation(summary = "检查密码是否在历史记录中", description = "检查用户的新密码是否在历史记录中出现过")
    public boolean isPasswordInHistory(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "待检查的密码") @RequestParam String password) {
        return passwordHistoryService.isPasswordInHistory(userId, password);
    }
} 