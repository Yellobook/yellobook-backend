package com.yellobook.domain.auth.service;

import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.domain.auth.dto.TokenResponse;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final RedisService redisService;
    private JwtService jwtService;

    public TokenResponse reissueToken(String refreshToken) {
        log.info("refreshToken 을 이용해 accessToken 재발급: {}", refreshToken);
        // refresh 토큰 만료여부 검사
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            log.info("refreshToken 만료: {}", refreshToken);
            throw new CustomException(AuthErrorCode.TOKEN_EXPIRED);
        }
        Long memberId = jwtService.getMemberIdFromRefreshToken(refreshToken);
        String redisRefreshToken = redisService.getRefreshToken(memberId);
        // 유효한 jwt 토큰이라면, 사용자의 refreshToken 과 대조한다.
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            log.warn("사용자의 refreshToken 이 아님: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }
        String newAccessToken = jwtService.createAccessToken(memberId);
        return TokenResponse.builder().accessToken(newAccessToken).build();
    }

    public void logout() {

    }

    public void deactivate() {
        // 멤버 상태 비활성화 & dirty checking
    }

    public void agreeTerm() {
        // 수정 & dirty checking
    }
}
