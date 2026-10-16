package com.exambaby.uhub.service.code.service.serviceImpl;

import com.exambaby.common.core.dto.RedisOnlineUser;
import com.exambaby.common.core.utils.RedisKeyUtils;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.service.OnlineUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {
    private final RedisTemplate<String, Object> redisTemplate;

    public OnlineUserServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 保存用户在线信息
     * @param user 在线用户信息
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    @Override
    public void save(RedisOnlineUser user,String systemCode, long timeout, TimeUnit unit) {
        String key = RedisKeyUtils.buildOnlineUserKey(systemCode, user.getUserId());
        redisTemplate.opsForValue().set(key, user, timeout, unit);
    }

    /**
     * 获取所有在线用户信息
     * @return 在线用户信息
     */
    @Override
    public List<RedisOnlineUser> getAllOnlineUsers() {
        Set<String> keys = redisTemplate.keys(RedisKeyUtils.ONLINE_USER_KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        return keys.stream()
                .map(key -> (RedisOnlineUser) redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 踢出用户
     * @param userID userId
     */
    @Override
    public void kickOut(String userID) {
        String systemCode = ThreadLocalContext.getSystemCode();
        // 可选：使用 pattern 匹配对应 token 的 key
        Set<String> keys = redisTemplate.keys(RedisKeyUtils.buildOnlineUserKey(systemCode, userID));
        if (keys != null) {
            keys.forEach(redisTemplate::delete);
        }
    }
}
