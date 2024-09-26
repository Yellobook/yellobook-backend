package com.yellobook.domains.auth.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisAuthService {
    private final StringRedisTemplate stringRedisTemplate;

    public void setRefreshToken(Long memberId, String value, long expiresIn) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = generateRefreshTokenKey(memberId);
        valueOps.set(key, value, Duration.ofSeconds(expiresIn));
    }

    public String getRefreshToken(Long memberId) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = generateRefreshTokenKey(memberId);
        return valueOps.get(key);
    }

    public void deleteRefreshToken(Long memberId) {
        String key = generateRefreshTokenKey(memberId);
        stringRedisTemplate.delete(key);
    }

    public void addTokenToBlacklist(String token, long expirationTime) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = generateBlackedTokenKey(token);
        valueOps.set(key, "", Duration.ofSeconds(expirationTime));
    }

    public Boolean isTokenInBlacklist(String token) {
        String key = generateBlackedTokenKey(token);
        return stringRedisTemplate.hasKey(key);
    }

    private String generateRefreshTokenKey(Long memberId) {
        return "auth:refresh:" + memberId;
    }

    private String generateBlackedTokenKey(String accessToken) {
        return "auth:blacked:" + accessToken;
    }
}
