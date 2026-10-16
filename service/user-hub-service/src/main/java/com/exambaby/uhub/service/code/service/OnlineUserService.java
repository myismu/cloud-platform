package com.exambaby.uhub.service.code.service;

import com.exambaby.common.core.dto.RedisOnlineUser;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface OnlineUserService {
    void save(RedisOnlineUser user,String systemCode, long timeout, TimeUnit unit);
    List<RedisOnlineUser> getAllOnlineUsers();
    void kickOut(String token);
}
