package com.exambaby.uhub.service.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.common.core.domain.ApiResponse;
import com.exambaby.uhub.service.code.dto.UsersDTO;
import com.exambaby.uhub.service.code.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "用户管理", description = "用户相关的所有接口，包括用户的增删改查等操作")
@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UsersService usersService;

    /**
     * 创建用户
     * @param usersDTO 用户信息
     * @return 创建结果
     */
    @Operation(
        summary = "创建用户",
        description = "创建一个新的用户账号"

    )
    @PostMapping("/")
    @ResponseBody
    public ApiResponse<String> createUser(@RequestBody UsersDTO usersDTO) {
        String message = usersService.saveUser(usersDTO);
        return new ApiResponse<>(200, message, null);
    }

    /**
     * 分页查询用户列表
     * @param current 当前页码
     * @param size 每页大小
     * @param username 用户名（可选）
     * @param email 邮箱（可选）
     * @param phoneNumber 电话号码（可选）
     * @param isLocked  是否锁定（可选）
     * @return 分页后的用户列表
     */
    @Operation(
        summary = "分页查询用户列表",
        description = "根据条件分页查询用户列表，支持多条件模糊查询"
    )
    @GetMapping("/page")
    @ResponseBody
    public ApiResponse<Page<UsersDTO>> getUsersByPage(
            @Parameter(description = "当前页码，从1开始") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页显示记录数") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "用户名，支持模糊查询") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱，支持模糊查询") @RequestParam(required = false) String email,
            @Parameter(description = "电话号码，支持模糊查询") @RequestParam(required = false) String phoneNumber,
            @Parameter(description = "用户状态，精确查询") @RequestParam(required = false) Integer isLocked) {
        Page<UsersDTO> page = new Page<>(current, size);
        Page<UsersDTO> result = usersService.getUsersByPage(page, username, email, phoneNumber, isLocked);
        return new ApiResponse<>(200, "查询成功", result);
    }

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @Operation(
        summary = "获取所有用户",
        description = "获取系统中所有用户的列表"
    )
    @GetMapping("getAllUsers")
    @ResponseBody
    public ApiResponse<List<UsersDTO>> getAllUsers() {
        List<UsersDTO> users = usersService.getAllUsers();
        return new ApiResponse<>(200, "查询成功", users);
    }

    /**
     * 根据ID获取用户.........
     * @param id 用户ID
     * @return 用户信息
     */
    @Operation(
        summary = "根据ID获取用户",
        description = "根据用户ID获取用户的详细信息"
    )
    @GetMapping("/{id}")
    @ResponseBody
    public ApiResponse<UsersDTO> getUserById(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        UsersDTO user = usersService.getUserById(id);
        return new ApiResponse<>(200, "查询成功", user);
    }

    /**
     * 根据ID获取用户名
     * @param id 用户ID
     * @return username
     */
    @GetMapping("/getUserNameById")
    public ApiResponse<String> getUserNameById(@RequestParam Long id) {
        String userName = usersService.getUserNameById(id);
        return new ApiResponse<>(200, "查询成功", userName);
    }

    /**
     * 根据用户ID列表批量获取用户名
     * @param ids 用户ID列表
     * @return userId -> username 的映射
     */
    @GetMapping("/getUserNamesByIds")
    public ApiResponse<Map<Long, String>> getUserNamesByIds(@RequestParam List<Long> ids) {
        Map<Long, String> userNames = usersService.getUserNamesByIds(ids);
        return new ApiResponse<>(200, "查询成功", userNames);
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param usersDTO 用户信息
     * @return 更新结果
     */
    @Operation(
        summary = "更新用户信息",
        description = "根据用户ID更新用户的信息"
    )
    @PutMapping("/{id}")
    @ResponseBody
    public ApiResponse<String> updateUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id,
            @RequestBody UsersDTO usersDTO) {
        usersDTO.setId(id);
        String message = usersService.updateUser(usersDTO);
        return new ApiResponse<>(200, message, null);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @Operation(
        summary = "删除用户",
        description = "根据用户ID删除指定的用户"
    )
    @DeleteMapping("/{id}")
    @ResponseBody
    public ApiResponse<String> deleteUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        usersService.deleteUser(id);
        return new ApiResponse<>(200, "删除用户成功", null);
    }
}
