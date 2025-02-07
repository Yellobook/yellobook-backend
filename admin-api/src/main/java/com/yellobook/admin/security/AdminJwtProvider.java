package com.yellobook.admin.security;

import com.yellobook.admin.support.error.AdminErrorType;
import com.yellobook.admin.support.error.AdminException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AdminJwtProvider {
    private final AdminJwtProperties properties;
    private final SecretKey accessTokenSecretKey;

    public AdminJwtProvider(AdminJwtProperties properties,
                            @Qualifier("adminAccessTokenSecretKey") SecretKey accessTokenSecretKey) {
        this.properties = properties;
        this.accessTokenSecretKey = accessTokenSecretKey;
    }

    public String createAccessToken(String sub, AdminMemberRole role) {
        return Jwts.builder()
                .subject(sub)
                .issuedAt(new Date(System.currentTimeMillis()))
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.accessToken()
                        .expiresIn() * 1000))
                .issuer("yellobook")
                .claim("role", role)
                .signWith(accessTokenSecretKey)
                .compact();
    }

    public Claims getPayloadFromAccessToken(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(accessTokenSecretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (JwtException e) {
            throw new AdminException(AdminErrorType.AUTH_FAILED);
        }
    }
}
