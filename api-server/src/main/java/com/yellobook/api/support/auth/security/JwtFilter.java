package com.yellobook.api.support.auth.security;

import com.yellobook.api.support.auth.AccessTokenPayload;
import com.yellobook.api.support.auth.JwtHttpExtractor;
import com.yellobook.api.support.auth.JwtProvider;
import com.yellobook.api.support.auth.JwtStorageHandler;
import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtStorageHandler jwtStorageHandler;
    private final JwtProvider jwtProvider;

    public JwtFilter(JwtStorageHandler jwtStorageHandler, JwtProvider jwtProvider) {
        this.jwtStorageHandler = jwtStorageHandler;
        this.jwtProvider = jwtProvider;
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Set<String> excludePaths = Set.of(
                "/api/v1/auth/token/reissue",
                "/api/v1/auth/terms",
                "/api/v1/dev"
        );
        String requestURI = request.getRequestURI();
        return excludePaths.stream()
                .anyMatch(requestURI::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = JwtHttpExtractor.extractAccessTokenFromHttpHeader(request);
        if (jwtStorageHandler.isAccessTokenInBlacklist(accessToken)) {
            throw new AuthException(AuthErrorType.ACCESS_TOKEN_BLACKLISTED);
        }

        AccessTokenPayload payload = jwtProvider.getPayloadFromAccessToken(accessToken);
        CustomUserDetails customUserDetails = new CustomUserDetails(payload.memberId(), payload.role());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
