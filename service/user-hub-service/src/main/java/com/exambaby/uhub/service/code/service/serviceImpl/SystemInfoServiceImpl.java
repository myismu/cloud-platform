package com.exambaby.uhub.service.code.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exambaby.common.core.domain.BusinessException;
import com.exambaby.uhub.service.code.dto.SystemInfoDTO;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import com.exambaby.uhub.service.code.mapper.SystemInfoMapper;
import com.exambaby.uhub.service.code.mapstruct.SystemInfoMapStruct;
import com.exambaby.uhub.service.code.service.SystemInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统服务实现类
 */
@Service
public class SystemInfoServiceImpl extends ServiceImpl<SystemInfoMapper, SystemInfo> implements SystemInfoService {

    private final SystemInfoMapStruct systemInfoMapStruct;

    public SystemInfoServiceImpl(SystemInfoMapStruct systemInfoMapStruct) {
        this.systemInfoMapStruct = systemInfoMapStruct;
    }

    /**
     * 创建系统
     * @param systemInfoDTO 系统信息
     * @return 系统信息DTO
     */
    @Override
    public SystemInfoDTO createSystem(SystemInfoDTO systemInfoDTO) {
        // 检查系统编码是否已存在
        LambdaQueryWrapper<SystemInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemInfo::getCode, systemInfoDTO.getCode());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException("系统编码已存在", 444);
        }

        // 转换为实体类并保存
        SystemInfo systemInfo = systemInfoMapStruct.toEntity(systemInfoDTO);
        this.save(systemInfo);
        return systemInfoMapStruct.toDTO(systemInfo);
    }

    /**
     * 更新系统信息
     * @param systemInfoDTO 系统信息
     * @return 系统信息DTO
     */
    @Override
    public SystemInfoDTO updateSystem(SystemInfoDTO systemInfoDTO) {
        // 检查系统是否存在
        SystemInfo existingSystem = this.getById(systemInfoDTO.getId());
        if (existingSystem == null) {
            throw new BusinessException("系统不存在", 444);
        }

        // 检查系统编码是否重复（排除当前系统）
        if (!existingSystem.getCode().equals(systemInfoDTO.getCode())) {
            LambdaQueryWrapper<SystemInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemInfo::getCode, systemInfoDTO.getCode());
            if (this.count(queryWrapper) > 0) {
                throw new BusinessException("系统编码已存在", 444);
            }
        }

        // 更新系统信息
        SystemInfo systemInfo = systemInfoMapStruct.toEntity(systemInfoDTO);
        this.updateById(systemInfo);
        return systemInfoMapStruct.toDTO(systemInfo);
    }

    /**
     * 删除系统
     * @param id 系统ID
     */
    @Override
    public void deleteSystem(Long id) {
        // 检查系统是否存在
        if (this.getById(id) == null) {
            throw new BusinessException("系统不存在", 444);
        }
        this.removeById(id);
    }

    /**
     * 根据ID获取系统信息
     * @param id 系统ID
     * @return 系统信息DTO
     */
    @Override
    public SystemInfoDTO getSystemById(Long id) {
        SystemInfo systemInfo = this.getById(id);
        return systemInfoMapStruct.toDTO(systemInfo);
    }

    /**
     * 分页查询系统信息
     * @param current 当前页码
     * @param size 每页大小
     * @param name 系统名称
     * @param code 系统编码
     * @return 分页系统信息DTO
     */
    @Override
    public Page<SystemInfoDTO> getSystemByPage(Integer current, Integer size, String name, String code) {
        // 创建分页对象
        Page<SystemInfo> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<SystemInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            queryWrapper.like(SystemInfo::getName, name);
        }
        if (StringUtils.hasText(code)) {
            queryWrapper.like(SystemInfo::getCode, code);
        }
        queryWrapper.orderByDesc(SystemInfo::getCreateTime);

        // 执行分页查询
        Page<SystemInfo> systemInfoPage = this.page(page, queryWrapper);

        // 转换为DTO并返回
        Page<SystemInfoDTO> dtoPage = new Page<>(systemInfoPage.getCurrent(), systemInfoPage.getSize(), systemInfoPage.getTotal());
        dtoPage.setRecords(systemInfoMapStruct.toDTOList(systemInfoPage.getRecords()));
        return dtoPage;
    }

    /**
     * 获取所有系统信息
     * @return 系统信息DTO列表
     */
    @Override
    public List<SystemInfoDTO> getAllSystems() {
        List<SystemInfo> list = this.list();
        return systemInfoMapStruct.toDTOList(list);
    }

//    /**
//     * 重写getOne方法，获取单个系统信息
//     * @param queryWrapper 查询条件
//     * @return  systems 系统信息
//     */
//    @Override
//    public SystemInfo getOne(Wrapper<SystemInfo> queryWrapper) {
//        SystemInfo systemInfo = super.getOne(queryWrapper);
//        if (systemInfo.getStatus() == 0){
//            throw new BusinessException("系统已停用,请联系管理员及时修复", 444);
//        }
//        return systemInfo;
//    }
}