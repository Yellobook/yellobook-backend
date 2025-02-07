package com.yellobook.admin.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class AdminJwtAuthorizationFilter extends OncePerRequestFilter {
    private final AdminJwtProvider adminJwtProvider;

    public AdminJwtAuthorizationFilter(AdminJwtProvider adminJwtProvider) {
        this.adminJwtProvider = adminJwtProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Set<String> excludePaths = Set.of(
                "/admin/**/auth/login"
        );
        String requestURI = request.getRequestURI();
        return excludePaths.stream()
                .anyMatch(requestURI::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<String> accessTokenOpt = AdminJwtHttpExtractor.extractAccessTokenFromHttpHeader(request);
        if (accessTokenOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = accessTokenOpt.get();
        Claims claims = adminJwtProvider.getPayloadFromAccessToken(accessToken);
        String sub = claims.getSubject();
        AdminMemberRole role = AdminMemberRole.valueOf(claims.get("role", String.class));

        AdminUserDetails adminUserDetails = new AdminUserDetails(sub, null, role);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminUserDetails,
                null,
                adminUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }
}
