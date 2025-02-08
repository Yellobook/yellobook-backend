package com.yellobook.api.security;

import com.yellobook.api.security.error.AuthErrorType;
import com.yellobook.api.security.error.AuthException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);
    private final JwtProperties properties;
    private final SecretKey accessTokenSecretKey;
    private final SecretKey refreshTokenSecretKey;

    public JwtProvider(JwtProperties properties,
                       @Qualifier("accessTokenSecretKey") SecretKey accessTokenSecretKey,
                       @Qualifier("refreshTokenSecretKey") SecretKey refreshTokenSecretKey) {
        this.properties = properties;
        this.accessTokenSecretKey = accessTokenSecretKey;
        this.refreshTokenSecretKey = refreshTokenSecretKey;
    }

    public String createAccessToken(long memberId) {
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.accessToken()
                        .expiresIn() * 1000))
                .signWith(accessTokenSecretKey)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .id(UUID.randomUUID()
                        .toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.refreshToken()
                        .expiresIn() * 1000))
                .signWith(refreshTokenSecretKey)
                .compact();
    }

    public long getMemberIdFromAccessToken(String accessToken) {
        try {
            return Long.parseLong(Jwts.parser()
                    .verifyWith(refreshTokenSecretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .getSubject());
        } catch (JwtException | NumberFormatException e) {
            log.error("AccessToken 이 올바른 형식이 아님", e);
            throw new AuthException(AuthErrorType.UNREADABLE_TOKEN);
        }
    }

    public Date getAccessTokenExpiresIn(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(accessTokenSecretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .getExpiration();
        } catch (JwtException e) {
            throw new AuthException(AuthErrorType.UNREADABLE_TOKEN);
        }
    }

    public Date getRefreshTokenExpiresIn(String refreshToken) {
        try {
            return Jwts.parser()
                    .verifyWith(refreshTokenSecretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload()
                    .getExpiration();
        } catch (JwtException e) {
            throw new AuthException(AuthErrorType.UNREADABLE_TOKEN);
        }
    }
}
