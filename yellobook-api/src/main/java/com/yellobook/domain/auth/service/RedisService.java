package com.yellobook.domain.auth.service;

import com.yellobook.domain.auth.dto.InvitationResponse;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest request;

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

    public String generateInvitationUrl(Long teamId, MemberTeamRole role) {
        String code = UUID.randomUUID().toString();
        String key = generateInvitaionKey(code);
        String value = teamId + ":" + role;

        // 15분 간 유효한 링크 설정
        //호스트 별로 다르게!
        //키를 도메인에 맞춰서
        stringRedisTemplate.opsForValue().set(key, value, 15, TimeUnit.MINUTES);
        String invitationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return invitationUrl + "/api/v1/invitation?code=" + key;
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

    private String generateRefreshTokenKey(Long memberId) {
        return "auth:refresh:" + memberId;
    }

    private String generateInvitaionKey(String code){
        return "team:invite:" + code;
    }
}
