package com.yellobook.api.security;

import com.yellobook.api.security.error.AuthErrorType;
import com.yellobook.api.security.error.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class JwtHttpExtractor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private JwtHttpExtractor() {
        throw new AssertionError();
    }

    public static String extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .map(String::strip)
                .filter(token -> token.startsWith(BEARER_PREFIX))
                .map(token -> token.substring(BEARER_PREFIX.length()))
                .orElseThrow(() -> new AuthException(AuthErrorType.ACCESS_TOKEN_NOT_FOUND_IN_HEADER));
    }

    public static String extractRefreshToken(HttpServletRequest request, String refreshTokenCookieName) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> cookie.getName()
                        .equals(refreshTokenCookieName))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthErrorType.REFRESH_TOKEN_NOT_FOUND_IN_COOKIES));
    }
}
