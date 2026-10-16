package com.exambaby.uhub.service.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.RoutesDTO;

import java.util.List;

/**
 * 路由服务接口，定义路由相关的业务方法
 */
public interface RoutesService {
    /**
     * 创建新路由
     *
     * @param routesDTO 路由信息
     * @return 创建成功的路由信息
     */
    RoutesDTO createRoute(RoutesDTO routesDTO);

    /**
     * 分页查询路由列表
     *
     * @param current 当前页码
     * @param size 每页大小
     * @param path 路由路径（可选，模糊查询）
     * @param name 路由名称（可选，模糊查询）
     * @param status 路由状态（可选，精确查询）
     * @return 分页后的路由列表
     */
    Page<RoutesDTO> getRoutesByPage(Integer current, Integer size, String path, String name, Integer status);

    /**
     * 获取所有路由列表
     *
     * @return 路由列表
     */
    List<RoutesDTO> getAllRoutes();

    /**
     * 根据ID获取路由信息
     *
     * @param id 路由ID
     * @return 路由信息
     */
    RoutesDTO getRouteById(Long id);

    /**
     * 更新路由信息
     *
     * @param id 路由ID
     * @param routesDTO 更新的路由信息
     * @return 更新后的路由信息
     */
    RoutesDTO updateRoute(Long id, RoutesDTO routesDTO);

    /**
     * 删除路由
     *
     * @param id 路由ID
     */
    void deleteRoute(Long id);

    /**
     * 获取路由树
     *
     * @return 树形结构的路由列表
     */
    List<RoutesDTO> getRouteTree();
} 