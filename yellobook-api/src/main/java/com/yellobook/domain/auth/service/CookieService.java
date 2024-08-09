package com.yellobook.domain.auth.service;

import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
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

    // 쿠키에서 refreshToken 추출
    public String getRefreshToken(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        // 쿠키에 refreshToken 이름에 해당하는 쿠키가 있는지 검색
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(refreshTokenName)) {
                refreshToken = cookie.getValue();
            }
        }
        if(refreshToken == null) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        return refreshToken;
    }

    // 로그인 직후 로컬스토리지로 이동시키기 때문에 만료시간을 짧게 설정
    private Cookie createTokenCookie(String cookieName, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        return cookie;
    }

    private void clearTokenCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

}
