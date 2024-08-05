package com.yellobook.domain.auth.security.filter;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domain.auth.service.JwtService;
import com.yellobook.domain.auth.service.RedisAuthService;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.error.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisAuthService redisAuthService;


    // 필터에서 제외시키고 싶은 request 에서 true 를 반환시킨다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Set<String> excludePaths = new HashSet<>(Set.of("/api/v1/auth/terms"));
        String requestURI = request.getRequestURI();
        return excludePaths.stream().anyMatch(requestURI::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtService.resolveAccessToken(request);
            if (!jwtService.isAccessTokenExpired(accessToken)
                    && !redisAuthService.isTokenInBlacklist(accessToken)
            ) {
                Member member = jwtService.getMemberFromAccessToken(accessToken);
                OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
                CustomOAuth2User customOAuth2User = new CustomOAuth2User(oauth2UserDTO);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        customOAuth2User,
                        null,
                        customOAuth2User.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (CustomException ex) {
             request.setAttribute("error", ex.getErrorCode());
        } catch (Exception e) {
            log.error("[AUTH_ERROR] 사용자 인가 과정에서 에러 발생: {}", e.getMessage());
            request.setAttribute("error", null);
        }
        // 로그인하지 않은 사용자 SecurityContext 에 없으므로 403
        filterChain.doFilter(request, response);
    }
}
