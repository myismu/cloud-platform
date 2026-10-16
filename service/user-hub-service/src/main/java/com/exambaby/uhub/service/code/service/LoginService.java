package com.exambaby.uhub.service.code.service;

import com.exambaby.uhub.service.code.dto.UsersDTO;


public interface LoginService {
//    登录
    public String login(UsersDTO usersDTO);

    /**
     * 重置密码
     * @param usersDTO 用户信息
     * @return 重置结果
     */
    String resetPassword(UsersDTO usersDTO);

    /**
     * 修改密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    String changePassword(String oldPassword, String newPassword);

    String register(UsersDTO usersDTO);
}
