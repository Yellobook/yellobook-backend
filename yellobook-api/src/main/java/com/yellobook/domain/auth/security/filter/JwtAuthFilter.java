package com.yellobook.domain.auth.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domain.auth.service.JwtService;
import com.yellobook.domain.auth.service.RedisAuthService;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.error.exception.CustomException;
import com.yellobook.response.ResponseFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisAuthService redisAuthService;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/api/v1/",
            "/api/v1/health",
            "/api/v1/auth/terms"
    );

    // 필터를 타지 않도록
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 쿼리파라미터를 포함하지 않은 URI 경로를 반환
        String requestURI = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(url -> url.equals(requestURI));
    }

    //
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.resolveToken(request);
        log.info(accessToken);
        OAuth2UserDTO oauth2UserDTO;

        // 로그인한 사용자
        try {
            if (accessToken != null
                    && !jwtService.isAccessTokenExpired(accessToken)
                    && !redisAuthService.isTokenInBlacklist(accessToken)
            ) {
                Member member = jwtService.getMemberFromAccessToken(accessToken);
                oauth2UserDTO = OAuth2UserDTO.from(member);
                CustomOAuth2User customOAuth2User = new CustomOAuth2User(oauth2UserDTO);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        customOAuth2User,
                        null,
                        customOAuth2User.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (CustomException ex) {
            log.warn(ex.getMessage());
            handleCustomException(response, ex);
            return;
        } catch (Error e) {
            // 에러핸들러 처리 추가 요망
            log.error(e.getMessage());
        }
        // 로그인하지 않은 사용자 SecurityContext 에 없으므로 403
        filterChain.doFilter(request, response);
    }

    private void handleCustomException(HttpServletResponse response, CustomException e) throws IOException {
        ResponseEntity<Object> errorResponse = ResponseFactory.failure(e.getErrorCode());
        // charset=UTF-8 을 붙이지 않으면 message 가 ??? 로 깨져서 표시
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(errorResponse.getStatusCode().value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse.getBody()));
        response.getWriter().flush();
    }
}
