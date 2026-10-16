package com.exambaby.uhub.service.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.common.core.domain.ApiResponse;
import com.exambaby.uhub.service.code.dto.RoutesDTO;
import com.exambaby.uhub.service.code.service.RoutesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 路由管理控制器
 */
@Log4j2
@RestController
@RequestMapping("/routes")
@Tag(name = "路由管理", description = "路由相关的接口，包括路由的增删改查等操作")
public class RoutesController {

    @Autowired
    private RoutesService routesService;

    @PostMapping
    @Operation(summary = "创建路由", description = "创建一个新的路由")
    public RoutesDTO createRoute(@RequestBody RoutesDTO routesDTO) {
        return routesService.createRoute(routesDTO);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询路由", description = "根据条件分页查询路由列表")
    public ApiResponse<Page<RoutesDTO>> getRoutesByPage(
            @Parameter(description = "当前页码，默认1") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小，默认10") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "路由路径，支持模糊查询") @RequestParam(required = false) String path,
            @Parameter(description = "路由名称，支持模糊查询") @RequestParam(required = false) String name,
            @Parameter(description = "路由状态，精确查询") @RequestParam(required = false) Integer status) {
        Page<RoutesDTO> routesByPage = routesService.getRoutesByPage(current, size, path, name, status);
        return new ApiResponse<>(200,"分页查询成功",routesByPage);
    }

    @GetMapping
    @Operation(summary = "获取所有路由", description = "获取系统中所有路由的列表")
    public List<RoutesDTO> getAllRoutes() {
        return routesService.getAllRoutes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取路由详情", description = "根据ID获取指定路由的详细信息")
    public RoutesDTO getRouteById(@Parameter(description = "路由ID") @PathVariable Long id) {
        return routesService.getRouteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新路由", description = "更新指定路由的信息")
    public RoutesDTO updateRoute(
            @Parameter(description = "路由ID") @PathVariable Long id,
            @RequestBody RoutesDTO routesDTO) {
        return routesService.updateRoute(id, routesDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除路由", description = "删除指定的路由")
    public void deleteRoute(@Parameter(description = "路由ID") @PathVariable Long id) {
        routesService.deleteRoute(id);
    }

    @GetMapping("/tree")
    @Operation(summary = "获取路由树", description = "获取树形结构的路由列表")
    public List<RoutesDTO> getRouteTree() {
        return routesService.getRouteTree();
    }
} 