package com.exambaby.uhub.service.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exambaby.uhub.service.code.dto.UsersDTO;
import com.exambaby.uhub.service.code.entity.Users;

import java.util.List;
import java.util.Map;

public interface UsersService {
    UsersDTO getUserById(Long id);

    String saveUser(UsersDTO usersDTO);

    List<UsersDTO> getAllUsers();

    Map<String, Object> getUserInfo(String userId);

    String updateUser(UsersDTO usersDTO);

    void deleteUser(Long id);

    /**
     * 分页查询用户列表，支持多条件模糊查询
     * @param page 分页参数
     * @param username 用户名（模糊查询）
     * @param email 邮箱（模糊查询）
     * @param phoneNumber 电话号码（模糊查询）
     * @param isLocked 状态（精确查询）
     * @return 分页后的用户列表
     */
    Page<UsersDTO> getUsersByPage(Page<UsersDTO> page, String username, String email, String phoneNumber, Integer isLocked);

    UsersDTO getPersonalInfo(String userId);

    boolean modifyPersonalInfo(UsersDTO usersDTO);

    String getUserNameById(Long id);

    Map<Long, String> getUserNamesByIds(List<Long> ids);

    void save(Users user);
}
