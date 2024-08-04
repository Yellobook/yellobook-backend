package com.yellobook.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String roleName;
}
