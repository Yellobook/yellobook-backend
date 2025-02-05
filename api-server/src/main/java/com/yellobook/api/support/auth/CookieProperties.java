package com.yellobook.api.support.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.cookie")
public record CookieProperties(
        int accessTokenCookieMaxAge,
        int refreshTokenCookieMaxAge,
        String accessTokenCookieName,
        String refreshTokenCookieName,
        String domain
) {
}
