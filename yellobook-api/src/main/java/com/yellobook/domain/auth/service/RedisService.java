package com.yellobook.domain.auth.service;

import com.yellobook.enums.MemberTeamRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void setRefreshToken(Long memberId , String value, long expiresIn) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = generateRefreshTokenKey(memberId);
        valueOps.set(key, value, Duration.ofSeconds(expiresIn));
    }

    public String getRefreshToken(Long memberId) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = generateRefreshTokenKey(memberId);
        return valueOps.get(key);
    }

    public String generateInvitationLink(Long teamId, MemberTeamRole role) {
        String key = "invitation:" + UUID.randomUUID().toString();
        String value = teamId + ":" + role;

        // 15분 간 유효한 링크 설정
        stringRedisTemplate.opsForValue().set(key, value, 15, TimeUnit.MINUTES);
        return "https://www.yellobook.site/invitation/" + key;
    }

    public String getInvitationInfo(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    public void deleteValue(Long memberId) {
        String key = generateRefreshTokenKey(memberId);
        stringRedisTemplate.delete(key);
    }

    public String generateRefreshTokenKey(Long memberId) {
        return "auth:refresh:" + memberId;
    }
}
