package com.yellobook.api.controller.auth;

import com.yellobook.api.controller.auth.dto.response.AcceptAllowanceResponse;
import com.yellobook.controller.auth.mapper.AuthMapper;
import com.yellobook.core.domain.member.Member;
import com.yellobook.member.repository.MemberRepository;
import com.yellobook.service.RedisAuthService;
import com.yellobook.support.error.code.AuthErrorCode;
import com.yellobook.support.error.code.CommonErrorCode;
import com.yellobook.support.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final RedisAuthService redisService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final AuthMapper authMapper;

    public AccessTokenRe reissueToken(String refreshToken) {
        log.info("[AUTH_INFO]refreshToken 을 이용해 accessToken 재발급: {}", refreshToken);
        // refresh 토큰 만료여부 검사
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            log.info("refreshToken 만료: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        Long memberId = jwtService.getMemberIdFromRefreshToken(refreshToken);
        String redisRefreshToken = redisService.getRefreshToken(memberId);
        // 유효한 jwt 토큰이라면, 사용자의 refreshToken 과 대조한다.
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            log.warn("[AUTH_WARN] 사용자의 refreshToken 이 아님: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }
        String newAccessToken = jwtService.createAccessToken(memberId);
    }

    public AcceptAllowanceResponse updateAllowance(String allowanceToken) {
        if (jwtService.isAllowanceTokenExpired(allowanceToken)) {
            log.info("약관 동의 토큰 만료: {}", allowanceToken);
            throw new CustomException(AuthErrorCode.TERMS_TOKEN_EXPIRED);
        }
        Member member = jwtService.getMemberFromAllowanceToken(allowanceToken);

        Long memberId = member.getId();
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        return new AcceptAllowanceResponse(accessToken, refreshToken);
    }

    public void logout(Long memberId, String accessToken) {
        try {
            // 엑세스 토큰의 남은 만료기간
            long expirationTime = jwtService.getAccessTokenExpirationTimeInMillis(accessToken);
            // 토큰을 블랙리스트에 추가
            redisService.addTokenToBlacklist(accessToken, expirationTime);

            // refreshToken 삭제
            redisService.deleteRefreshToken(memberId);
        } catch (Exception e) {
            log.error("[AUTH_ERROR] 로그아웃 중 오류 발생: {}", e.getMessage());
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
