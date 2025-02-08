package com.yellobook.api.security;

import com.yellobook.api.security.error.AuthErrorType;
import com.yellobook.api.security.error.AuthException;
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

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtProvider adminJwtProvider;

    public JwtAuthorizationFilter(JwtService jwtService, JwtProvider adminJwtProvider) {
        this.jwtService = jwtService;
        this.adminJwtProvider = adminJwtProvider;
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        Set<String> excludePaths = Set.of(
                "/api/v1/auth/refresh",
                "/api/v1/dev"
        );
        String requestURI = request.getRequestURI();
        return excludePaths.stream()
                .anyMatch(requestURI::startsWith);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = JwtHttpExtractor.extractAccessToken(request);
        if (jwtService.isAccessTokenInBlacklist(accessToken)) {
            throw new AuthException(AuthErrorType.ACCESS_TOKEN_BLACKLISTED);
        }

        long memberId = adminJwtProvider.getMemberIdFromAccessToken(accessToken);
        CustomUserDetails customUserDetails = new CustomUserDetails(memberId);
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
