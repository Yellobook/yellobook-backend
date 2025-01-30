package com.yellobook.api.support.auth;

import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class JwtHttpExtractor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private JwtHttpExtractor() {
        throw new AssertionError();
    }

    public static String extractAccessTokenFromHttpHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        throw new AuthException(AuthErrorType.ACCESS_TOKEN_NOT_FOUND_IN_HEADER);
    }
}
