package com.yellobook.core.domain.common;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;

public enum TeamMemberRole {
    SELLER("판매자"),
    ORDERER("주문자"),
    VIEWER("뷰어");

    private final String description;

    TeamMemberRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static TeamMemberRole fromDescription(String description) {
        for (TeamMemberRole role : values()) {
            if (role.description.equals(description)) {
                return role;
            }
        }
        throw new CoreException(CoreErrorType.ROLE_DESCRIPTION_NOT_FOUND, description);
    }
}

