package com.yellobook.api.support.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {
    private final CookieProperties properties;

    public CookieProvider(CookieProperties properties) {
        this.properties = properties;
    }

    public Cookie createAccessTokenCookie(String token) {
        return createTokenCookie(properties.accessTokenCookieName(), properties.accessTokenCookieMaxAge(), token);
    }

    public Cookie createRefreshTokenCookie(String token) {
        return createTokenCookie(properties.refreshTokenCookieName(), properties.refreshTokenCookieMaxAge(), token);
    }

    private Cookie createTokenCookie(String cookieName, int maxAge, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setDomain(properties.domain());
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        return cookie;
    }
}
