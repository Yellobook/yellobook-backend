package com.yellobook.domain.auth.oauth2;

import com.yellobook.domain.auth.jwt.JwtService;
import com.yellobook.domain.auth.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.auth.oauth2.utils.CookieUtil;
import com.yellobook.enums.MemberRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @Value("${frontend.auth-redirect-url}")
    private String authRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String email = customUserDetails.getName();
        MemberRole role = customUserDetails.getRole();
        // jwt 생성
        String accessToken = jwtService.createAccessToken(email, role);
        // String refreshToken = jwtService.createRefreshToken(email);
        response.addCookie(cookieUtil.createAccessTokenCookie(accessToken));
        response.sendRedirect(authRedirectUrl);
    }
}
