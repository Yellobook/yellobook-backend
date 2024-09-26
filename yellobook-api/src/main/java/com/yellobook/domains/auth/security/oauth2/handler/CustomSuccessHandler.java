package com.yellobook.domains.auth.security.oauth2.handler;

import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.service.CookieService;
import com.yellobook.domains.auth.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Value("${frontend.auth-redirect-url}")
    private String authRedirectUrl;

    @Value("${frontend.allowance-redirect-url}")
    private String allowanceRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOauth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customOauth2User.getMemberId();
        Boolean allowance = customOauth2User.getAllowance();

        // 약관에 동의하지 않은 사용자라면
        if (!allowance) {
            // 임시 토큰 발급 (15분)
            String allowanceToken = jwtService.createAllowanceToken(memberId);
            // 약관 동의 페이지로 리다이렉트
            String redirectUrl = allowanceRedirectUrl + "?token=" + allowanceToken;
            response.sendRedirect(redirectUrl);
            return;
        }

        // jwt 생성
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        // 쿠키에 jwt 추가
        response.addCookie(cookieService.createAccessTokenCookie(accessToken));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
        response.sendRedirect(authRedirectUrl);
    }
}
