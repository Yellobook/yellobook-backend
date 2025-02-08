package com.yellobook.admin.support;

import com.yellobook.admin.domain.AdminMember;
import com.yellobook.admin.domain.AdminMemberRepository;
import com.yellobook.admin.security.AdminUserDetails;
import com.yellobook.admin.support.error.AdminErrorType;
import com.yellobook.admin.support.error.AdminException;
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
public class AdminMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AdminMemberRepository adminMemberRepository;

    public AdminMemberArgumentResolver(AdminMemberRepository adminMemberRepository) {
        this.adminMemberRepository = adminMemberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == AdminMember.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AdminException(AdminErrorType.AUTH_ERROR);
        }

        if (!(authentication.getPrincipal() instanceof AdminUserDetails adminUserDetails)) {
            log.error("Principal 객체가 AdminUserDetails 타입이 아님 - Principal class: {}", authentication.getPrincipal()
                    .getClass());
            throw new AdminException(AdminErrorType.AUTH_ERROR);
        }

        String sub = adminUserDetails.sub();

        AdminMember adminMember = adminMemberRepository.findBySub(sub)
                .orElseThrow(() -> {
                    log.error("인가 정보가 올바르지 않습니다. sub: {}", sub);
                    return new AdminException(AdminErrorType.AUTH_ERROR);
                });
        adminMemberRepository.updateLastLoginAt(adminMember.adminId());

        return adminMember;
    }
}
