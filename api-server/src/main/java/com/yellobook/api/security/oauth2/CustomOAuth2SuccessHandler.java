package com.yellobook.api.security.oauth2;

import com.yellobook.api.security.CookieProvider;
import com.yellobook.api.security.JwtProvider;
import com.yellobook.api.security.error.AuthErrorType;
import com.yellobook.api.security.error.AuthException;
import com.yellobook.core.domain.member.Member;
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

    private static final String DEFAULT_BIO = "등록된 한줄소개가 없습니다.";
    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);

    private final MemberService memberService;
    private final CookieProvider cookieManager;
    private final RedirectProperties redirectProperties;
    private final JwtProvider jwtProvider;
    private final TermsService termsService;

    public CustomOAuth2SuccessHandler(MemberService memberService, CookieProvider cookieManager,
                                      RedirectProperties redirectProperties, JwtProvider jwtProvider,
                                      TermsService termsService) {
        this.memberService = memberService;
        this.cookieManager = cookieManager;
        this.redirectProperties = redirectProperties;
        this.jwtProvider = jwtProvider;
        this.termsService = termsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
        SocialInfo socialInfo = extractSocialInfo(oAuth2User);
        ProfileInfo profileInfo = extractProfileInfo(oAuth2User);

        memberService.findByEmail(socialInfo.email())
                .ifPresentOrElse(
                        member -> handleExistingMember(response, member),
                        () -> handleNewMember(response, profileInfo, socialInfo)
                );
    }

    private void handleExistingMember(HttpServletResponse response, Member member) {
        try {
            long memberId = member.memberId();
            boolean hasAgreedToTerms = termsService.hasMemberAgreedToActiveTerms(memberId);

            String redirectUrl = hasAgreedToTerms
                    ? redirectProperties.authenticatedRedirectUrl()
                    : redirectProperties.termsRedirectUrl() + "?memberId=" + memberId;

            response.addCookie(cookieManager.createAccessTokenCookie(jwtProvider.createAccessToken(memberId)));
            response.addCookie(cookieManager.createRefreshTokenCookie(jwtProvider.createRefreshToken()));
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.warn("소셜로그인 후 클라이언트로 리다이렉트 실패", e);
            throw new AuthException(AuthErrorType.AUTHORIZE_FAILED);
        }
    }

    private void handleNewMember(HttpServletResponse response, ProfileInfo profileInfo, SocialInfo socialInfo) {
        try {
            long memberId = memberService.create(profileInfo, socialInfo);
            log.info("신규 회원 가입: memberId: {}, nickname: {}, email: {}", memberId, profileInfo.nickname(),
                    socialInfo.email());

            response.addCookie(cookieManager.createAccessTokenCookie(jwtProvider.createAccessToken(memberId)));
            response.addCookie(cookieManager.createRefreshTokenCookie(jwtProvider.createRefreshToken()));
            response.sendRedirect(redirectProperties.authenticatedRedirectUrl());
        } catch (Exception e) {
            log.warn("소셜로그인 후 클라이언트로 리다이렉트 실패", e);
            throw new AuthException(AuthErrorType.AUTHORIZE_FAILED);
        }
    }

    private SocialInfo extractSocialInfo(OAuth2User user) {
        Map<String, Object> attributes = user.getAttributes();
        return new SocialInfo(
                requireNonNull(attributes.get("oauthId"), "OAuth ID가 없습니다."),
                requireNonNull(attributes.get("name"), "제공자가 없습니다."),
                requireNonNull(attributes.get("email"), "이메일이 없습니다.")
        );
    }

    private ProfileInfo extractProfileInfo(OAuth2User user) {
        Map<String, Object> attributes = user.getAttributes();
        return new ProfileInfo(
                requireNonNull(attributes.get("nickname"), "닉네임이 없습니다."),
                DEFAULT_BIO,
                requireNonNull(attributes.get("profileImage"), "프로필 이미지가 없습니다."),
                null
        );
    }

    private String requireNonNull(Object value, String errorMessage) {
        if (value == null) {
            throw new AuthException(AuthErrorType.INVALID_OAUTH2_RESPONSE, errorMessage);
        }
        return String.valueOf(value);
    }
}
