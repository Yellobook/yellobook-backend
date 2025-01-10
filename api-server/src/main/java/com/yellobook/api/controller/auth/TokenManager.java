package com.yellobook.api.controller.auth;

import com.yellobook.api.controller.auth.enums.TokenType;
import com.yellobook.support.error.code.AuthErrorCode;
import com.yellobook.support.error.code.CommonErrorCode;
import com.yellobook.support.error.exception.Co;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class TokenManager {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenRepository tokenRepository;

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

    public TokenManager(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @PostConstruct
    public void initialize() {
        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
        allowanceTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(allowanceSecret));
    }

    public String extractAccessTokenFromHttpHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.info("[AUTH_INFO] 요청 헤더에 JWT 토큰이 존재하지 않음");
        throw new Co(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }

    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessTokenExpiresIn, accessTokenSecretKey);
    }

    public String createRefreshToken(Long memberId) {
        String refreshToken = createToken(memberId, refreshTokenExpiresIn, refreshTokenSecretKey);
        tokenRepository.setRefreshToken(memberId, refreshToken, refreshTokenExpiresIn);
        return refreshToken;
    }

    public String createAllowanceToken(Long memberId) {
        return createToken(memberId, allowanceTokenExpiresIn, allowanceTokenSecretKey);
    }

    public long getAccessTokenExpirationTimeInMillis(String token) {
        return extractAll(token, accessTokenSecretKey)
                .getExpiration()
                .getTime() - System.currentTimeMillis();
    }

    public Long getMemberIdFromToken(String token, TokenType tokenType) {
        validateTokenExpiration(token, tokenType);
        var secretKey = switch (tokenType) {
            case ACCESS -> accessTokenSecretKey;
            case REFRESH -> refreshTokenSecretKey;
            case TERMS -> allowanceTokenSecretKey;
        };
        return parseMemberId(token, secretKey);
    }

    private Long parseMemberId(String token, SecretKey secretKey) {
        return extractAll(token, secretKey)
                .get("memberId", Long.class);
    }

    private Claims extractAll(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
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

    private boolean validateTokenExpiration(String token, TokenType tokenType) {
        try {
            var secretKey = switch (tokenType) {
                case ACCESS -> accessTokenSecretKey;
                case REFRESH -> refreshTokenSecretKey;
                case TERMS -> allowanceTokenSecretKey;
            };
            return extractAll(token, secretKey)
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                log.info("{} 만료: {}", tokenType.getDescription(), e.getMessage());
                // exception 던지나?
            }
            if (e instanceof MalformedJwtException) {
                log.warn("{} 의 형식이 올바르지 않음: {}", tokenType.getDescription(), e.getMessage());
                throw new Co(AuthErrorCode.INVALID_TOKEN_FORMAT);
            } else if (e instanceof SignatureException) {
                log.warn("{} 의 서명이 일치하지 않음: {}", e.getMessage());
                throw new Co(AuthErrorCode.INVALID_TOKEN_SIGNATURE);
            } else if (e instanceof UnsupportedJwtException) {
                log.warn("{} 의 특정 헤더나 클레임이 지원되지 않음: {}", tokenType.getDescription(), e.getMessage());
                throw new Co(AuthErrorCode.UNSUPPORTED_TOKEN);
            } else {
                log.error("{} 의 만료 검사중 알 수 없는 오류 발생: {}", tokenType.getDescription(), e.getMessage());
                throw new Co(CommonErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
