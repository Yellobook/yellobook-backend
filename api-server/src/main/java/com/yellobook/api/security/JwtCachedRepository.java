package com.yellobook.api.security;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JwtCachedRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public JwtCachedRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected void saveRefreshToken(String refreshToken, Long memberId, Duration duration) {
        redisTemplate.opsForValue()
                .set(refreshToken, memberId.toString(), duration);
    }

    protected Optional<Long> findMemberIdFromRefreshToken(String refreshToken) {
        String memberId = redisTemplate.opsForValue()
                .get(refreshToken);
        return Optional.ofNullable(memberId)
                .map(Long::valueOf);
    }

    protected void addAccessTokenToBlacklist(String accessToken, Date expiration) {
        String key = "auth:blacklist:" + accessToken;
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue()
                    .set(key, "", ttl, TimeUnit.MILLISECONDS);
        }
    }

    protected void removeRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    protected Boolean isAccessTokenInBlacklist(String accessToken) {
        String key = "auth:blacklist:" + accessToken;
        return redisTemplate.hasKey(key);
    }
}
