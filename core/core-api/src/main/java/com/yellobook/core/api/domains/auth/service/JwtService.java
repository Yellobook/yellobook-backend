package com.yellobook.core.api.domains.auth.service;

import com.yellobook.core.api.domains.auth.enums.TokenType;
import com.yellobook.core.domains.member.entity.Member;
import com.yellobook.core.domains.member.repository.MemberRepository;
import com.yellobook.core.api.support.error.code.AuthErrorCode;
import com.yellobook.core.api.support.error.code.CommonErrorCode;
import com.yellobook.core.api.support.error.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
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

    @PostConstruct
    public void initialize() {
        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
        allowanceTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(allowanceSecret));
    }

    /**
     * 요청 헤더에서 JWT 액세스 토큰을 추출한다.
     *
     * @param request HTTP 요청 객체
     * @return JWT 액세스 토큰 문자열
     */
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.info("[AUTH_INFO] 요청 헤더에 JWT 토큰이 존재하지 않음");
        throw new CustomException(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }

    /**
     * 사용자 ID로 JWT 액세스 토큰을 생성한다.
     *
     * @param memberId 사용자 ID
     * @return 생성된 JWT 액세스 토큰 문자열
     */
    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessTokenExpiresIn, accessTokenSecretKey);
    }

    /**
     * 사용자 ID로 JWT 리프레시 토큰을 생성한다.
     *
     * @param memberId 사용자 ID
     * @return 생성된 JWT 리프레시 토큰 문자열
     */
    public String createRefreshToken(Long memberId) {
        String refreshToken = createToken(memberId, refreshTokenExpiresIn, refreshTokenSecretKey);
        redisService.setRefreshToken(memberId, refreshToken, refreshTokenExpiresIn);
        return refreshToken;
    }

    /**
     * 사용자 ID로 JWT 약관 동의 토큰을 생성한다.
     *
     * @param memberId 사용자 ID
     * @return 생성된 JWT 약관 동의 토큰 문자열
     */
    public String createAllowanceToken(Long memberId) {
        return createToken(memberId, allowanceTokenExpiresIn, allowanceTokenSecretKey);
    }

    /**
     * 약관 동의 토큰이 만료되었는지 확인한다.
     *
     * @param allowanceToken 약관 동의 토큰
     * @return 토큰이 만료되었는지 여부
     */
    public boolean isAllowanceTokenExpired(String allowanceToken) {
        return isTokenExpired(allowanceToken, allowanceTokenSecretKey, TokenType.TERMS);
    }

    /**
     * 액세스 토큰이 만료되었는지 확인한다.
     *
     * @param accessToken 액세스 토큰
     * @return 토큰이 만료되었는지 여부
     */
    public boolean isAccessTokenExpired(String accessToken) {
        return isTokenExpired(accessToken, accessTokenSecretKey, TokenType.ACCESS);
    }

    /**
     * 리프레시 토큰이 만료되었는지 확인한다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 토큰이 만료되었는지 여부
     */
    public boolean isRefreshTokenExpired(String refreshToken) {
        return isTokenExpired(refreshToken, refreshTokenSecretKey, TokenType.REFRESH);
    }

    /**
     * 유효한 액세스 토큰에서 사용자 정보를 추출한다. 토큰 만료 여부 검사 필요.
     *
     * @param token JWT 액세스 토큰
     * @return 액세스 토큰에서 추출된 사용자 정보
     */
    public Member getMemberFromAccessToken(String token) {
        Long memberId = getMemberIdFromAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("[AUTH_ERROR] 유효한 엑세스 토큰에서 추출된 사용자 ID: {}에 해당하는 사용자가 존재하지 않음", memberId);
                    return new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
                });
    }

    /**
     * 약관 동의 토큰의 만료 시간을 밀리초 단위로 반환한다.
     *
     * @param token JWT 엑세스 토큰
     * @return 토큰의 남은 유효 기간 (밀리초 단위)
     */
    public long getAccessTokenExpirationTimeInMillis(String token) {
        return extractAll(token, accessTokenSecretKey)
                .getExpiration()
                .getTime() - System.currentTimeMillis();
    }

    /**
     * 유효한 약관 동의 토큰에서 사용자 정보를 추출한다. 토큰 만료 여부 검사 필요.
     *
     * @param token JWT 약관 동의 토큰
     * @return 약관 동의 토큰에서 추출된 사용자 정보
     */
    public Member getMemberFromAllowanceToken(String token) {
        Long memberId = getMemberIdFromAllowanceToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("[AUTH_ERROR] 유효한 약관 동의 토큰에서 추출된 사용자 ID: {}에 해당하는 사용자가 존재하지 않음", memberId);
                    return new CustomException(AuthErrorCode.USER_NOT_EXIST);
                });
    }

    /**
     * 약관 동의 토큰에서 사용자 ID를 추출한다.
     *
     * @param token JWT 약관 동의 토큰
     * @return 약관 동의 토큰에서 추출된 사용자 ID
     */
    public Long getMemberIdFromAllowanceToken(String token) {
        return parseMemberId(token, allowanceTokenSecretKey);
    }

    /**
     * 액세스 토큰에서 사용자 ID를 추출한다.
     *
     * @param token JWT 액세스 토큰
     * @return 액세스 토큰에서 추출된 사용자 ID
     */
    public Long getMemberIdFromAccessToken(String token) {
        return parseMemberId(token, accessTokenSecretKey);
    }

    /**
     * 리프레시 토큰에서 사용자 ID를 추출한다.
     *
     * @param token JWT 리프레시 토큰
     * @return 리프레시 토큰에서 추출된 사용자 ID
     */
    public Long getMemberIdFromRefreshToken(String token) {
        return parseMemberId(token, refreshTokenSecretKey);
    }

    /**
     * JWT 토큰의 모든 클레임을 추출한다.
     *
     * @param token     JWT 토큰
     * @param secretKey JWT 비밀키
     * @return JWT 토큰에서 추출된 클레임
     */
    private Claims extractAll(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 사용자 ID를 포함하는 JWT 토큰을 생성한다.
     *
     * @param memberId  사용자 ID
     * @param expiresIn 토큰 유효 기간 (초 단위)
     * @param secretKey JWT 비밀키
     * @return 생성된 JWT 토큰
     */
    private String createToken(Long memberId, long expiresIn, SecretKey secretKey) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiresIn * 1000))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 ID를 추출한다.
     *
     * @param token     JWT 토큰
     * @param secretKey JWT 비밀키
     * @return JWT 토큰에서 추출된 사용자 ID
     */
    private Long parseMemberId(String token, SecretKey secretKey) {
        return extractAll(token, secretKey)
                .get("memberId", Long.class);
    }

    /**
     * JWT 토큰이 만료되었는지 확인한다.
     *
     * @param token     JWT 토큰
     * @param secretKey JWT 비밀키
     * @return 토큰이 만료되었는지 여부
     */
    private boolean isTokenExpired(String token, SecretKey secretKey, TokenType tokenType) {
        try {
            return extractAll(token, secretKey)
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                log.info("[AUTH_INFO] JWT 토큰이 만료: {}", e.getMessage());
                switch (tokenType) {
                    case ACCESS -> throw new CustomException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
                    case REFRESH -> throw new CustomException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
                    case TERMS -> throw new CustomException(AuthErrorCode.TERMS_TOKEN_EXPIRED);
                }
            }
            if (e instanceof MalformedJwtException) {
                log.warn("[AUTH_WARNING] JWT 토큰 형식이 올바르지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.INVALID_TOKEN_FORMAT);
            } else if (e instanceof SignatureException) {
                log.warn("[AUTH_WARNING] JWT 토큰의 서명이 일치하지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.INVALID_TOKEN_SIGNATURE);
            } else if (e instanceof UnsupportedJwtException) {
                log.warn("[AUTH_WARNING] JWT 토큰의 특정 헤더나 클레임이 지원되지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.UNSUPPORTED_TOKEN);
            } else {
                log.error("[AUTH_ERROR] JWT 토큰 만료 검사중 알 수 없는 오류 발생: {}", e.getMessage());
                throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
