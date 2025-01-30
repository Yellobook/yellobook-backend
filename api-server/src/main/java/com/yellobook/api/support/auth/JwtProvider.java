package com.yellobook.api.support.auth;

import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
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

    public String createAccessToken(AccessTokenPayload payload) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.accessToken()
                        .expiresIn() * 1000))
                .claim("memberId", payload.memberId())
                .claim("role", payload.role())
                .signWith(accessTokenSecretKey)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.refreshToken()
                        .expiresIn() * 1000))
                .claim("memberId", memberId)
                .signWith(refreshTokenSecretKey)
                .compact();
    }

    public AccessTokenPayload getPayloadFromAccessToken(String accessToken) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(refreshTokenSecretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            return new AccessTokenPayload(
                    payload.get("memberId", Long.class),
                    payload.get("role", AppMemberRole.class)
            );
        } catch (JwtException e) {

            throw new AuthException(AuthErrorType.UNREADABLE_TOKEN);
        }
    }

    public Long getMemberIdFromRefreshToken(String refreshToken) {
        try {
            return Jwts.parser()
                    .verifyWith(refreshTokenSecretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload()
                    .get("memberId", Long.class);
        } catch (JwtException e) {
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
}
