package com.yellobook.domain.auth.service;

import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.error.exception.CustomException;
import io.jsonwebtoken.*;
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
    private final RedisAuthService redisService;
    private final MemberRepository memberRepository;

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.expires-in}")
    private long accessTokenExpiresIn;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.expires-in}")
    private long refreshTokenExpiresIn;

    @Value("${jwt.allowance.secret}")
    private String allowanceSecret;

    @Value("${jwt.allowance.expires-in}")
    private long allowanceTokenExpiresIn;

    private SecretKey accessTokenSecretKey;
    private SecretKey refreshTokenSecretKey;
    private SecretKey allowanceTokenSecretKey;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    public void initialize() {
        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
        allowanceTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(allowanceSecret));
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

    public String createAllowanceToken(Long memberId) {
        return createToken(memberId, allowanceTokenExpiresIn, allowanceTokenSecretKey);
    }

    public boolean isAllowanceTokenExpired(String allowanceToken) throws CustomException {
        return isTokenExpired(allowanceToken, allowanceTokenSecretKey);
    }

    public boolean isAccessTokenExpired(String accessToken) throws CustomException {
        return isTokenExpired(accessToken, accessTokenSecretKey);
    }

    public boolean isRefreshTokenExpired(String refreshToken) throws CustomException {
        return isTokenExpired(refreshToken, refreshTokenSecretKey);
    }


    public Member getMemberFromAccessToken(String token) {
        Long memberId = getMemberIdFromAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));
    }
    public Member getMemberFromAllowanceToken(String token) {
        Long memberId = getMemberIdFromAllowanceToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_EXIST));
    }

    public long getAllowanceTokenExpirationTimeInMillis(String token) {
        return extractAll(token, allowanceTokenSecretKey)
                .getExpiration().getTime() - System.currentTimeMillis();
    }

    public Long getMemberIdFromAllowanceToken(String token) {
        return parseMemberId(token, allowanceTokenSecretKey);
    }

    public Long getMemberIdFromAccessToken(String token) {
        return parseMemberId(token, accessTokenSecretKey);
    }

    public Long getMemberIdFromRefreshToken(String token) {
        return parseMemberId(token, refreshTokenSecretKey);
    }

    private Claims extractAll(String token, SecretKey secretKey) {
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String createToken(Long memberId, long expiresIn, SecretKey secretKey) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiresIn * 1000))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    private Long parseMemberId(String token, SecretKey secretKey) {
        return extractAll(token,secretKey)
                .get("memberId",Long.class);
    }

    private boolean isTokenExpired(String token, SecretKey secretKey) {
        try {
            return extractAll(token,secretKey)
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
}
