package com.yellobook.common.resolver;

import com.yellobook.common.resolver.annotation.TeamMember;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class TeamMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final RedisTeamService redisTeamService;

    // 메소드 파라미터가 resolver 에 의해 지원될지 여부를 판단
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 애노테이션이 있고
        boolean hasTeamMemberAnnotation = parameter.hasParameterAnnotation(TeamMember.class);
        // TeamMemberVO 타입인지 체크
        boolean isMemberTeamVOType = TeamMemberVO.class.isAssignableFrom(parameter.getParameterType());
        return hasTeamMemberAnnotation && isMemberTeamVOType;
    }

    // 매개변수로 넣어줄 값을 제공
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomOAuth2User customOauth2User = (CustomOAuth2User) authentication.getPrincipal();
            Long memberId = customOauth2User.getMemberId();
            TeamMemberVO teamMember = redisTeamService.getCurrentTeamMember(memberId);
            return teamMember;
        }
        // 인증되지 않은 경로에 컨트롤러에서 사용한다면 접근 거부
        throw new CustomException(AuthErrorCode.ACCESS_DENIED);
    }
}
