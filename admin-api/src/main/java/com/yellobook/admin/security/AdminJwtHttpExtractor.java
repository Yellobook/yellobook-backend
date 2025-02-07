package com.yellobook.admin.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.util.StringUtils;

public class AdminJwtHttpExtractor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private AdminJwtHttpExtractor() {
        throw new AssertionError();
    }

    public static Optional<String> extractAccessTokenFromHttpHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(StringUtils::hasText)
                .filter(token -> token.startsWith(BEARER_PREFIX))
                .map(token -> token.substring(BEARER_PREFIX.length()));
    }
}

