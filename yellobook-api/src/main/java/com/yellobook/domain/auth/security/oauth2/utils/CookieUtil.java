package com.yellobook.domain.auth.security.oauth2.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${cookie.max-age}")
    private int maxAge;

    @Value("${cookie.domain}")
    private String domain;

    @Value("${cookie.access-name}")
    private String accessTokenName;

    @Value("${cookie.refresh-name}")
    private String refreshTokenName;

    public Cookie createAccessTokenCookie(String token) {
        return createTokenCookie(accessTokenName, token);
    }

    public Cookie createRefreshTokenCookie(String token) {
        return createTokenCookie(refreshTokenName, token);
    }

    public void clearAccessAndRefreshTokenCookie(HttpServletResponse response) {
        clearTokenCookie(accessTokenName, response);
        clearTokenCookie(refreshTokenName, response);
    }

    // 로그인 직후 로컬스토리지로 이동시키기 때문에 만료시간을 짧게 설정
    private Cookie createTokenCookie(String cookieName, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        return cookie;
    }

    private void clearTokenCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

}
