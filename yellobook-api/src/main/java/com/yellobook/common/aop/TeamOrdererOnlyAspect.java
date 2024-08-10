package com.yellobook.common.aop;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TeamOrdererOnlyAspect {
    RedisTeamService redisTeamService;

    @Before("@annotation(com.yellobook.common.annotation.TeamOrdererOnly)")
    public void checkIsTeamAdmin(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = principal.getMemberId();
        /**
         * 컨트롤러 애노테이션이므로
         * AOP 에서 예외가 발생하면 컨트롤러 계층으로 전달되어 @RestControllerAdvice 에서 처리한다.
         */
        TeamMemberVO teamMember = redisTeamService.getCurrentTeamMember(memberId);
        if(!teamMember.getRole().equals(MemberTeamRole.ORDERER)){
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }
    }
}
