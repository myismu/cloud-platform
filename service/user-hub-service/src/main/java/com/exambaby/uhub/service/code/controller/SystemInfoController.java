package com.exambaby.uhub.service.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.SystemInfoDTO;
import com.exambaby.uhub.service.code.service.serviceImpl.SystemInfoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统管理控制器
 */
@Tag(name = "系统管理", description = "系统管理相关接口")
@RestController
@RequestMapping("/systemInfo")
public class SystemInfoController {

    private final SystemInfoServiceImpl systemInfoService;

    public SystemInfoController(SystemInfoServiceImpl systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    @Operation(summary = "新增系统")
    @PostMapping
    public SystemInfoDTO add(@RequestBody SystemInfoDTO systemInfoDTO) {
        return systemInfoService.createSystem(systemInfoDTO);
    }

    @Operation(summary = "修改系统")
    @PutMapping
    public SystemInfoDTO update(@RequestBody SystemInfoDTO systemInfoDTO) {
        return systemInfoService.updateSystem(systemInfoDTO);
    }

    @Operation(summary = "删除系统")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        systemInfoService.deleteSystem(id);
    }

    @Operation(summary = "获取系统详情")
    @GetMapping("/{id}")
    public SystemInfoDTO getById(@PathVariable Long id) {
        return systemInfoService.getSystemById(id);
    }

    @Operation(summary = "分页查询系统列表")
    @GetMapping("/page")
    public Page<SystemInfoDTO> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {
        return systemInfoService.getSystemByPage(current, size, name, code);
    }

    @Operation(summary = "获取所有系统")
    @GetMapping("/list")
    public List<SystemInfoDTO> list() {
        return systemInfoService.getAllSystems();
    }
}