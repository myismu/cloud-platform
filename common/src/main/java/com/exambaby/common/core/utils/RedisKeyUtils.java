package com.exambaby.common.core.utils;

public final class RedisKeyUtils {
    public static final String ONLINE_USER_KEY_PREFIX = "online:";

    private RedisKeyUtils() {
    }

    public static String buildOnlineUserKey(String systemCode, Object userId) {
        return ONLINE_USER_KEY_PREFIX + systemCode + ":" + userId;
    }
}
