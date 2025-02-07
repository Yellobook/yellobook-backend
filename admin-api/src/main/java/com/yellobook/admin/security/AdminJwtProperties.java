package com.yellobook.admin.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.admin.jwt")
public record AdminJwtProperties(
        AccessToken accessToken
) {
    public record AccessToken(
            String secret,
            long expiresIn
    ) {
    }
}
