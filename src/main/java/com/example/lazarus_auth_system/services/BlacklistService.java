package com.example.lazarus_auth_system.services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class BlacklistService {

    private final StringRedisTemplate redis;
    private static final int LIMIT = 5;
    private final Duration blockTime = Duration.ofMinutes(5);

    public BlacklistService(StringRedisTemplate redis) {
        this.redis = redis;
    }


    public void track(String ip) {
        String key = "login:fail:" + ip;
        Long count = redis.opsForValue().increment(key);
        System.out.println("\n\n" + count);
        redis.expire(key, blockTime);
    }

    public boolean isBlocked(String ip) {
        String key = "login:fail:" + ip;
        String value = redis.opsForValue().get(key);
        return value != null && Long.parseLong(value) >= LIMIT;
    }

    public void resetAttempts(String ip) {
        String key = getLoginFailKey(ip);
        redis.delete(key);
    }

    private String getLoginFailKey(String ip) {
        return "login:fail:" + ip;
    }

    public void blacklistToken(String token, long expirationInSeconds) {
        String key = getTokenKey(token);
        redis.opsForValue().set(key, "1", Duration.ofSeconds(expirationInSeconds));
    }

    public boolean isTokenBlacklisted(String token) {
        String key = getTokenKey(token);
        return Boolean.TRUE.equals(redis.hasKey(key));
    }

    private String getTokenKey(String token) {
        return "blacklist:token:" + token;
    }

}
