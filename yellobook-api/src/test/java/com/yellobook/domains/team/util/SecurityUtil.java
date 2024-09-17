package com.yellobook.domains.team.util;

import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static void setAuthentication(CustomOAuth2User customOAuth2User) {
        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        customOAuth2User,
                        null,
                        customOAuth2User.getAuthorities()
                );

        // SecurityContextHolder에 설정
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}
