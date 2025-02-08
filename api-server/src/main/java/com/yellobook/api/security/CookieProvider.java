package com.yellobook.api.security;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {
    private final CookieProperties properties;

    public CookieProvider(CookieProperties properties) {
        this.properties = properties;
    }

    public Cookie createAccessTokenCookie(String accessToken) {

        Cookie cookie = new Cookie(properties.access()
                .name(), accessToken);
        cookie.setMaxAge(properties.access()
                .maxAge());
        cookie.setPath("/");
        cookie.setDomain(properties.domain());
        cookie.setHttpOnly(properties.access()
                .httpOnly());
        cookie.setSecure(properties.access()
                .secure());
        return cookie;
    }

    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(properties.refresh()
                .name(), refreshToken);
        cookie.setMaxAge(properties.refresh()
                .maxAge());
        cookie.setPath(properties.refresh()
                .path());
        cookie.setDomain(properties.domain());
        cookie.setHttpOnly(properties.refresh()
                .httpOnly());
        cookie.setSecure(properties.refresh()
                .secure());
        return cookie;
    }
}
