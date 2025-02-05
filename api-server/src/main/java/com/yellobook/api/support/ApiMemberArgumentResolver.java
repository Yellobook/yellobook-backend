package com.yellobook.api.support;

import com.yellobook.api.support.auth.AppMemberRole;
import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import com.yellobook.api.support.auth.security.CustomUserDetails;
import com.yellobook.api.support.error.ApiErrorType;
import com.yellobook.api.support.error.ApiException;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ApiMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ApiMemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == ApiMember.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("인가 정보가 존재하지 않음");
            throw new AuthException(AuthErrorType.ACCESS_DENIED);
        }

        if (!(authentication.getPrincipal() instanceof CustomUserDetails customUserDetails)) {
            log.error("Principal 객체가 CustomUserDetails 타입이 아님 - Principal class: {}", authentication.getPrincipal()
                    .getClass());
            throw new AuthException(AuthErrorType.ACCESS_DENIED);
        }

        Long memberId = customUserDetails.getMemberId();
        AppMemberRole role = customUserDetails.getRole();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("인가되었지만, 존재하지 않는 사용자  - memberId: {}", memberId);
                    return new ApiException(ApiErrorType.INTERNAL_SERVER_ERROR);
                });
        return new ApiMember(
                memberId,
                member.socialInfo(),
                member.profileInfo()
        );
    }
}
