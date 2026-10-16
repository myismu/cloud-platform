package com.exambaby.uhub.service.code.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.common.core.domain.BusinessException;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.dto.RoutesDTO;
import com.exambaby.uhub.service.code.entity.Routes;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import com.exambaby.uhub.service.code.mapper.RoutesMapper;
import com.exambaby.uhub.service.code.mapstruct.RoutesMapStruct;
import com.exambaby.uhub.service.code.service.RoutesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路由服务实现类
 */
@Log4j2
@Service
public class RoutesServiceImpl implements RoutesService {

    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private final SystemInfoServiceImpl systemInfoService;
    @Autowired
    private RoutesMapStruct routesMapStruct;

    public RoutesServiceImpl(SystemInfoServiceImpl systemInfoService, RoutesMapStruct routesMapStruct) {
        this.systemInfoService = systemInfoService;
        this.routesMapStruct = routesMapStruct;
    }

    @Override
    public RoutesDTO createRoute(RoutesDTO routesDTO) {
        //        如果没有系统代码就代表是用户请求的操作，则获取当前线程的系统代码
        if (routesDTO.getSystemId() == null) {
            //系统代码
            String systemCode = ThreadLocalContext.getSystemCode();
            //获取系统信息
            SystemInfo systemInfo = systemInfoService.getOne(new LambdaQueryWrapper<SystemInfo>().eq(SystemInfo::getCode, systemCode));
//        设置系统
            routesDTO.setSystemId(systemInfo.getId());
        }

        // 检查路由路径是否已存在
        LambdaQueryWrapper<Routes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Routes::getPath, routesDTO.getPath());
        if (routesMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("路由路径已存在",444);
        }

        // 转换为实体类并保存
        Routes routes = routesMapStruct.toEntity(routesDTO);
        routesMapper.insert(routes);
        return routesMapStruct.toDTO(routes);
    }

    @Override
    public Page<RoutesDTO> getRoutesByPage(Integer current, Integer size, String path, String name, Integer status) {
        // 创建分页对象
        Page<Routes> page = new Page<>(current, size);
        
        // 构建查询条件
        LambdaQueryWrapper<Routes> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(path)) {
            queryWrapper.like(Routes::getPath, path);
        }
        if (StringUtils.hasText(name)) {
            queryWrapper.like(Routes::getName, name);
        }
        if (status != null) {
            queryWrapper.eq(Routes::getStatus, status);
        }
        
        // 执行分页查询
        Page<Routes> routesPage = routesMapper.selectPage(page, queryWrapper);
        
        // 转换为DTO并返回
        Page<RoutesDTO> dtoPage = new Page<>(routesPage.getCurrent(), routesPage.getSize(), routesPage.getTotal());
        dtoPage.setRecords(routesMapStruct.toDTOList(routesPage.getRecords()));
        return dtoPage;
    }

    @Override
    public List<RoutesDTO> getAllRoutes() {
        List<Routes> routes = routesMapper.selectList(null);
        return routesMapStruct.toDTOList(routes);
    }

    @Override
    public RoutesDTO getRouteById(Long id) {
        Routes routes = routesMapper.selectById(id);
        if (routes == null) {
            throw new BusinessException("路由不存在",444);
        }
        return routesMapStruct.toDTO(routes);
    }

    @Override
    public RoutesDTO updateRoute(Long id, RoutesDTO routesDTO) {
        // 检查路由是否存在
        Routes existingRoute = routesMapper.selectById(id);
        if (existingRoute == null) {
            throw new BusinessException("路由不存在",444);
        }
        // 检查路由路径是否重复（排除当前路由）
        if (!existingRoute.getPath().equals(routesDTO.getPath())) {
            LambdaQueryWrapper<Routes> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Routes::getPath, routesDTO.getPath());
            if (routesMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("路由路径已存在",444);
            }
        }

        // 更新路由信息
        Routes routes = routesMapStruct.toEntity(routesDTO);
        routes.setId(id);
        routesMapper.updateById(routes);
        return routesMapStruct.toDTO(routes);
    }

    @Override
    public void deleteRoute(Long id) {
        // 检查路由是否存在
        if (routesMapper.selectById(id) == null) {
            throw new BusinessException("路由不存在",444);
        }
        routesMapper.deleteById(id);
    }

    @Override
    public List<RoutesDTO> getRouteTree() {
        // 获取所有路由
        List<Routes> allRoutes = routesMapper.selectList(null);
        List<RoutesDTO> allRoutesDTO = routesMapStruct.toDTOList(allRoutes);
        // 构建路由树结构
        Map<Long, List<RoutesDTO>> parentIdMap = allRoutesDTO.stream()
                .collect(Collectors.groupingBy(RoutesDTO::getParentId));

        // 获取根路由（parentId为null或0的路由）
        List<RoutesDTO> rootRoutes = parentIdMap.getOrDefault(0L, new ArrayList<>());
        if (rootRoutes.isEmpty()) {
            rootRoutes = parentIdMap.get(null);
        }

        // 递归设置子路由
        rootRoutes.forEach(route -> setChildren(route, parentIdMap));

        return rootRoutes;
    }

    /**
     * 递归设置子路由
     */
    private void setChildren(RoutesDTO parent, Map<Long, List<RoutesDTO>> parentIdMap) {
        List<RoutesDTO> children = parentIdMap.get(parent.getId());
        if (children != null && !children.isEmpty()) {
            children.forEach(child -> setChildren(child, parentIdMap));
            parent.setChildren(children);
        }
    }
} 