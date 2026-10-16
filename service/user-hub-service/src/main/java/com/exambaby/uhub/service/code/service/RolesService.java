package com.exambaby.uhub.service.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.RolesDTO;

import java.util.List;

/**
 * 角色服务接口，定义角色相关的业务方法
 */
public interface RolesService {
    /**
     * 创建新角色
     *
     * @param rolesDTO 角色信息
     * @return 创建成功的角色信息
     */
    RolesDTO createRole(RolesDTO rolesDTO);

    /**
     * 分页查询角色列表
     *
     * @param current 当前页码
     * @param size 每页大小
     * @param roleName 角色名称（可选，模糊查询）
     * @param description 角色描述（可选，模糊查询）
     * @param status 角色状态（可选，精确查询）
     * @return 分页后的角色列表
     */
    Page<RolesDTO> getRolesByPage(Integer current, Integer size, String roleName, String description, Integer status);

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    List<RolesDTO> getAllRoles();

    /**
     * 根据ID获取角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
    RolesDTO getRoleById(Long id);

    /**
     * 更新角色信息
     *
     * @param id 角色ID
     * @param rolesDTO 更新的角色信息
     * @return 更新后的角色信息
     */
    RolesDTO updateRole(Long id, RolesDTO rolesDTO);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void deleteRole(Long id);
} 