package com.exambaby.uhub.service.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.RolesDTO;
import com.exambaby.uhub.service.code.service.RolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/roles")
@Tag(name = "角色管理", description = "角色相关的接口，包括角色的增删改查等操作")
public class RolesController {

    @Autowired
    private RolesService rolesService;

    @PostMapping
    @Operation(summary = "创建角色", description = "创建一个新的角色")
    public RolesDTO createRole(@RequestBody RolesDTO rolesDTO) {
        return rolesService.createRole(rolesDTO);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询角色", description = "根据条件分页查询角色列表")
    public Page<RolesDTO> getRolesByPage(
            @Parameter(description = "当前页码，默认1") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小，默认10") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "角色名称，支持模糊查询") @RequestParam(required = false) String roleName,
            @Parameter(description = "角色描述，支持模糊查询") @RequestParam(required = false) String description,
            @Parameter(description = "角色状态，精确查询") @RequestParam(required = false) Integer status) {
        return rolesService.getRolesByPage(current, size, roleName, description, status);
    }

    @GetMapping
    @Operation(summary = "获取所有角色", description = "获取系统中所有角色的列表")
    public List<RolesDTO> getAllRoles() {
        return rolesService.getAllRoles();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情", description = "根据ID获取指定角色的详细信息")
    public RolesDTO getRoleById(@Parameter(description = "角色ID") @PathVariable Long id) {
        return rolesService.getRoleById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新角色", description = "更新指定角色的信息")
    public RolesDTO updateRole(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @RequestBody RolesDTO rolesDTO) {
        return rolesService.updateRole(id, rolesDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "删除指定的角色")
    public void deleteRole(@Parameter(description = "角色ID") @PathVariable Long id) {
        rolesService.deleteRole(id);
    }
} 