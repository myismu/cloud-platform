package com.exambaby.uhub.service.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exambaby.uhub.service.code.dto.SystemInfoDTO;
import com.exambaby.uhub.service.code.entity.SystemInfo;

import java.util.List;

/**
 * 系统服务接口
 */

public interface SystemInfoService extends IService<SystemInfo> {
    /**
     * 创建系统
     * @param systemInfoDTO 系统信息
     * @return 创建后的系统信息
     */
    SystemInfoDTO createSystem(SystemInfoDTO systemInfoDTO);

    /**
     * 更新系统
     * @param systemInfoDTO 系统信息
     * @return 更新后的系统信息
     */
    SystemInfoDTO updateSystem(SystemInfoDTO systemInfoDTO);

    /**
     * 删除系统
     * @param id 系统ID
     */
    void deleteSystem(Long id);

    /**
     * 获取系统详情
     * @param id 系统ID
     * @return 系统信息
     */
    SystemInfoDTO getSystemById(Long id);

    /**
     * 分页查询系统列表
     * @param current 当前页码
     * @param size 每页大小
     * @param name 系统名称
     * @param code 系统编码
     * @return 分页后的系统列表
     */
    Page<SystemInfoDTO> getSystemByPage(Integer current, Integer size, String name, String code);

    /**
     * 获取所有系统
     * @return 系统列表
     */
    List<SystemInfoDTO> getAllSystems();

}