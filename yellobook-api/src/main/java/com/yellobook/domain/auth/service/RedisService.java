package com.yellobook.domain.auth.service;

import com.yellobook.domain.auth.dto.InvitationResponse;
import com.yellobook.enums.MemberTeamRole;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
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

    public InvitationResponse getInvitationInfo(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new CustomException(TeamErrorCode.INVITATION_NOT_FOUND);
        }

        // value 형식이 "teamId:role" 인지 확인하고 분리
        String[] parts = value.split(":");
        if (parts.length != 2) {
            throw new CustomException(TeamErrorCode.INVALID_INVITATION);
        }

        Long teamId = Long.parseLong(parts[0]);
        MemberTeamRole role = MemberTeamRole.valueOf(parts[1]);

        return new InvitationResponse(teamId, role);
    }

    public void deleteValue(Long memberId) {
        String key = generateRefreshTokenKey(memberId);
        stringRedisTemplate.delete(key);
    }

    public String generateRefreshTokenKey(Long memberId) {
        return "auth:refresh:" + memberId;
    }
}
