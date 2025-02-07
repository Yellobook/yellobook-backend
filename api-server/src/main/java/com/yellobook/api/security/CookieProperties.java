package com.yellobook.api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.client.cookie")
public record CookieProperties(
        AccessTokenCookieProperties access,
        RefreshTokenCookieProperties refresh,
        String domain
) {
    record AccessTokenCookieProperties(
            String name,
            int maxAge,
            boolean httpOnly,
            boolean secure
    ) {
    }

    record RefreshTokenCookieProperties(
            String name,
            int maxAge,
            boolean httpOnly,
            boolean secure,
            String path
    ) {
    }
}
