package com.yellobook.api.support.auth;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CachedJwtRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public CachedJwtRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected void saveRefreshToken(Long memberId, String refreshToken, Duration duration) {
        String key = "auth:refresh:" + memberId;
        redisTemplate.opsForValue()
                .set(key, refreshToken, duration);
    }

    protected Optional<String> findRefreshToken(Long memberId) {
        String key = "auth:refresh:" + memberId;
        String refreshToken = redisTemplate.opsForValue()
                .get(key);
        return Optional.ofNullable(refreshToken);
    }

    protected void removeRefreshToken(Long memberId) {
        String key = "auth:refresh:" + memberId;
        redisTemplate.delete(key);
    }

    protected void addAccessTokenToBlacklist(String accessToken, Date expiration) {
        String key = "auth:blacklist:" + accessToken;
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue()
                    .set(key, "", ttl, TimeUnit.MILLISECONDS);
        }
    }

    protected Boolean isAccessTokenInBlacklist(String accessToken) {
        String key = "auth:blacklist:" + accessToken;
        return redisTemplate.hasKey(key);
    }
}
