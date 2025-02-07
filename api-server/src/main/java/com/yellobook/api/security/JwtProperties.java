package com.yellobook.api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.client.jwt")
public record JwtProperties(
        AccessToken accessToken,
        RefreshToken refreshToken
) {
    public record AccessToken(
            String secret,
            long expiresIn
    ) {
    }

    public record RefreshToken(
            String secret,
            long expiresIn
    ) {
    }
}
