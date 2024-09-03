package com.yellobook.domains.auth.utils;

import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JwtUtil {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 요청 헤더에서 JWT 액세스 토큰을 추출한다.
     *
     * @param request HTTP 요청 객체
     * @return JWT 액세스 토큰 문자열
     */
    public static String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.info("[AUTH_INFO] 요청 헤더에 JWT 토큰이 존재하지 않음");
        throw new CustomException(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }
}
