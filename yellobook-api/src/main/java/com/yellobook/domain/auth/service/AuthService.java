package com.yellobook.domain.auth.service;

import com.yellobook.domain.auth.dto.response.AllowanceResponse;
import com.yellobook.domain.auth.dto.response.RefreshResponse;
import com.yellobook.domain.auth.mapper.AuthMapper;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.error.code.AuthErrorCode;
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
    private final RedisAuthService redisService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final AuthMapper authMapper;

    public RefreshResponse reissueToken(String refreshToken) {
        log.info("refreshToken 을 이용해 accessToken 재발급: {}", refreshToken);
        // refresh 토큰 만료여부 검사
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            log.info("refreshToken 만료: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        Long memberId = jwtService.getMemberIdFromRefreshToken(refreshToken);
        String redisRefreshToken = redisService.getRefreshToken(memberId);
        // 유효한 jwt 토큰이라면, 사용자의 refreshToken 과 대조한다.
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            log.warn("사용자의 refreshToken 이 아님: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }
        String newAccessToken = jwtService.createAccessToken(memberId);
        return RefreshResponse.builder().accessToken(newAccessToken).build();
    }


    @Transactional
    public AllowanceResponse updateAllowance(String allowanceToken) {
        if (jwtService.isAllowanceTokenExpired(allowanceToken)) {
            log.info("약관 동의 토큰 만료: {}", allowanceToken);
            throw new CustomException(AuthErrorCode.TERMS_TOKEN_EXPIRED);
        }
        Member member = jwtService.getMemberFromAllowanceToken(allowanceToken);
        member.updateAllowance();
        memberRepository.save(member);
        Long memberId = member.getId();
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        return authMapper.toAllowanceResponse(accessToken, refreshToken);
    }

    public void logout(String accessToken, Long memberId) {
        Long tokenMemberId = jwtService.getMemberIdFromAccessToken(accessToken);
        //  본인 확인
        if (!tokenMemberId.equals(memberId)) {
            log.warn("사용자 :{} 가 본인이 아닌 사용자에 대한 로그아웃 요청", tokenMemberId);
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }
        long expirationTime = jwtService.getAllowanceTokenExpirationTimeInMillis(accessToken);

        // 토큰을 블랙리스트에 추가
        redisService.addTokenToBlacklist(accessToken, expirationTime);

        // refreshToken 삭제
        redisService.deleteRefreshToken(memberId);
    }

    public void deactivate(CustomOAuth2User oAuth2User) {
        // 멤버 상태 비활성화 & dirty checking
    }

}
