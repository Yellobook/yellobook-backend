package com.yellobook.api.support.auth.security.oauth2;

import com.yellobook.api.support.auth.AccessTokenPayload;
import com.yellobook.api.support.auth.AppMemberRole;
import com.yellobook.api.support.auth.CookieProvider;
import com.yellobook.api.support.auth.JwtProvider;
import com.yellobook.api.support.auth.security.RedirectProperties;
import com.yellobook.api.support.error.ApiErrorType;
import com.yellobook.api.support.error.ApiException;
import com.yellobook.core.domain.member.MemberService;
import com.yellobook.core.domain.member.ProfileInfo;
import com.yellobook.core.domain.member.SocialInfo;
import com.yellobook.core.domain.terms.TermsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MemberService memberService;
    private final TermsService termsService;
    private final JwtProvider jwtProvider;
    private final CookieProvider cookieManager;
    private final RedirectProperties redirectProperties;

    public CustomOAuth2SuccessHandler(MemberService memberService, TermsService termsService, JwtProvider jwtProvider,
                                      CookieProvider cookieManager, RedirectProperties redirectProperties) {
        this.memberService = memberService;
        this.termsService = termsService;
        this.jwtProvider = jwtProvider;
        this.cookieManager = cookieManager;
        this.redirectProperties = redirectProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User user = token.getPrincipal();
            Map<String, Object> attributes = user.getAttributes();
            String provider = token.getAuthorizedClientRegistrationId();

            SocialInfo socialInfo = new SocialInfo(
                    (String) attributes.get("id"),
                    provider,
                    (String) attributes.get("email")
            );

            ProfileInfo profileInfo = new ProfileInfo(
                    (String) attributes.get("nickname"),
                    null,
                    (String) attributes.get("profileImage"),
                    null
            );

            Long memberId = memberService.getIdOrRegister(profileInfo, socialInfo);
            log.info("소셜 로그인 성공 - provider: {}, memberId: {}", provider, memberId);

            handleAuthenticatedUser(response, memberId, provider);
        } catch (IOException e) {
            log.error("소셜 로그인 성공 후 리다이렉트 실패", e);
            throw new ApiException(ApiErrorType.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleAuthenticatedUser(HttpServletResponse response, Long memberId, String provider)
            throws IOException {
        if (!termsService.hasMemberAgreedToActiveTerms(memberId)) {
            response.sendRedirect(redirectProperties.termRedirectUrl());
            return;
        }

        String accessToken = jwtProvider.createAccessToken(new AccessTokenPayload(memberId, AppMemberRole.ROLE_USER));
        String refreshToken = jwtProvider.createRefreshToken(memberId);

        response.addCookie(cookieManager.createAccessTokenCookie(accessToken));
        response.addCookie(cookieManager.createRefreshTokenCookie(refreshToken));
        response.sendRedirect(redirectProperties.authenticatedRedirectUrl());
    }


}
