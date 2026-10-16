package com.exambaby.uhub.service.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.LoginHistoryDTO;
import com.exambaby.uhub.service.code.service.LoginHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/login-history")
@Tag(name = "登录历史记录管理", description = "管理用户登录历史记录")
public class LoginHistoryController {

    private final LoginHistoryService loginHistoryService;

    public LoginHistoryController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    @PostMapping
    @Operation(summary = "保存登录历史记录", description = "记录用户登录信息")
    public String saveLoginHistory(@RequestBody LoginHistoryDTO loginHistoryDTO) {
        return loginHistoryService.saveLoginHistory(loginHistoryDTO);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户登录历史", description = "根据用户ID获取登录历史记录")
    public List<LoginHistoryDTO> getLoginHistoryByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return loginHistoryService.getLoginHistoryByUserId(userId);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询登录历史", description = "分页查询登录历史记录，支持按用户ID、IP地址、用户名和客户端类型筛选")
    public Page<LoginHistoryDTO> getLoginHistoryByPage(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "IP地址") @RequestParam(required = false) String ipAddress,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "客户端类型") @RequestParam(required = false) String clientType) {
        Page<LoginHistoryDTO> page = new Page<>(current, size);
        return loginHistoryService.getLoginHistoryByPage(page, userId, ipAddress, username, clientType);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "删除登录历史记录", description = "根据记录ID删除登录历史")
    public String deleteLoginHistory(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        return loginHistoryService.deleteLoginHistory(id);
    }
} 