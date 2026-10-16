package com.exambaby.uhub.service.code.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.common.core.domain.BusinessException;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.dto.RolesDTO;
import com.exambaby.uhub.service.code.entity.Roles;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import com.exambaby.uhub.service.code.mapper.RolesMapper;
import com.exambaby.uhub.service.code.mapstruct.RolesMapStruct;
import com.exambaby.uhub.service.code.service.RolesService;
import com.exambaby.uhub.service.code.service.SystemInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 角色服务实现类
 */
@Service
public class RolesServiceImpl implements RolesService {

    private final RolesMapper rolesMapper;

    private final RolesMapStruct rolesMapStruct;

    private final SystemInfoService systemInfoService;
    public RolesServiceImpl(RolesMapper rolesMapper, RolesMapStruct rolesMapStruct, SystemInfoService systemInfoService) {
        this.rolesMapper = rolesMapper;
        this.rolesMapStruct = rolesMapStruct;
        this.systemInfoService = systemInfoService;
    }


    /**
     * 创建角色
     * @param rolesDTO 角色信息
     * @return 角色DTO
     */
    @Override
    public RolesDTO createRole(RolesDTO rolesDTO) {
        //如果没有系统代码就代表是用户请求的操作，则获取当前线程的系统代码
        if (rolesDTO.getSystemId() == null) {
            String systemCode = ThreadLocalContext.getSystemCode();
            SystemInfo systemInfo = systemInfoService.getOne(new LambdaQueryWrapper<SystemInfo>().eq(SystemInfo::getCode, systemCode));
            rolesDTO.setSystemId(systemInfo.getId());
        }

        // 检查角色名是否已存在
        LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Roles::getRoleName, rolesDTO.getRoleName());
        if (rolesMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("角色名已存在",444);
        }

        // 转换为实体类并保存
        Roles roles = rolesMapStruct.toEntity(rolesDTO);
        rolesMapper.insert(roles);
        return rolesMapStruct.toDTO(roles);
    }

    @Override
    public Page<RolesDTO> getRolesByPage(Integer current, Integer size, String roleName, String description, Integer status) {
        // 创建分页对象
        Page<Roles> page = new Page<>(current, size);
        
        // 构建查询条件
        LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(roleName)) {
            queryWrapper.like(Roles::getRoleName, roleName);
        }
        if (StringUtils.hasText(description)) {
            queryWrapper.like(Roles::getDescription, description);
        }
        if (status != null) {
            queryWrapper.eq(Roles::getStatus, status);
        }
        // 执行分页查询
        Page<Roles> rolesPage = rolesMapper.selectPage(page, queryWrapper);
        
        // 转换为DTO并返回
        Page<RolesDTO> dtoPage = new Page<>(rolesPage.getCurrent(), rolesPage.getSize(), rolesPage.getTotal());
        dtoPage.setRecords(rolesMapStruct.toDTOList(rolesPage.getRecords()));
        return dtoPage;
    }

    @Override
    public List<RolesDTO> getAllRoles() {
        List<Roles> roles = rolesMapper.selectList(null);
        return rolesMapStruct.toDTOList(roles);
    }

    @Override
    public RolesDTO getRoleById(Long id) {
        Roles roles = rolesMapper.selectById(id);
        if (roles == null) {
            throw new BusinessException("角色不存在",444);
        }
        return rolesMapStruct.toDTO(roles);
    }

    @Override
    public RolesDTO updateRole(Long id, RolesDTO rolesDTO) {
        // 检查角色是否存在
        Roles existingRole = rolesMapper.selectById(id);
        if (existingRole == null) {
            throw new BusinessException("角色不存在",444);
        }

        // 检查角色名是否重复（排除当前角色）
        if (!existingRole.getRoleName().equals(rolesDTO.getRoleName())) {
            LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Roles::getRoleName, rolesDTO.getRoleName());
            if (rolesMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("角色名已存在",444);
            }
        }

        // 更新角色信息
        Roles roles = rolesMapStruct.toEntity(rolesDTO);
        roles.setId(id);
        rolesMapper.updateById(roles);
        return rolesMapStruct.toDTO(roles);
    }

    @Override
    public void deleteRole(Long id) {
        // 检查角色是否存在
        if (rolesMapper.selectById(id) == null) {
            throw new BusinessException("角色不存在",444);
        }
        rolesMapper.deleteById(id);
    }
} 