package com.yellobook.core.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("관리자"),
    USER("사용자");

    private final String roleName;
}
