package com.yellobook.domain.auth.service;

import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.error.exception.CustomException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final RedisService redisService;
    MemberRepository memberRepository;

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.expires-in}")
    private long accessTokenExpiresIn;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.expires-in}")
    private long refreshTokenExpiresIn;

    private SecretKey accessTokenSecretKey;
    private SecretKey refreshTokenSecretKey;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    public void initialize() {
        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        // 에러 처리 추가 요망
        return null;
    }

    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessTokenExpiresIn, accessTokenSecretKey);
    }

    public String createRefreshToken(Long memberId) {
        String refreshToken = createToken(memberId, refreshTokenExpiresIn, refreshTokenSecretKey);
        redisService.setRefreshToken(memberId, refreshToken, refreshTokenExpiresIn);
        return refreshToken;
    }

    public boolean isAccessTokenExpired(String accessToken) throws CustomException {
        return isTokenExpired(accessToken, accessTokenSecretKey);
    }

    public boolean isRefreshTokenExpired(String refreshToken) throws CustomException {
        return isTokenExpired(refreshToken, refreshTokenSecretKey);
    }

    // 토큰에서 memberId 를 추출해 사용자를 가져온다.
    public Member getMemberFromAccessToken(String token) {
        Long memberId = getMemberIdFromAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));
    }

    // 토큰의 만료여부 검사 후 만료되지 않았다면 호출할 것
    public Long getMemberIdFromAccessToken(String token) {
        return Jwts.parser().verifyWith(accessTokenSecretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId",Long.class);
    }

    public Long getMemberIdFromRefreshToken(String token) {
        return Jwts.parser().verifyWith(refreshTokenSecretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId",Long.class);
    }


    private boolean isTokenExpired(String token, SecretKey secretKey) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            if (e instanceof MalformedJwtException) {
                log.warn("올바르지 않은 형식의 JWT 토큰: {}", e.getMessage());
            } else if (e instanceof SignatureException) {
                log.warn("JWT 서명이 일치하지 않음: {}", e.getMessage());
            } else if (e instanceof UnsupportedJwtException) {
                log.warn("토큰의 특정 헤더나 클레임이 지원되지 않음: {}", e.getMessage());
            } else {
                log.warn("JWT 만료기간 검사 처리 중 오류 발생: {}", e.getMessage());
            }
            throw new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private String createToken(Long memberId, long expiresIn, SecretKey secretKey) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiresIn * 1000))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }


}
