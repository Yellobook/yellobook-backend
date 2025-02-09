package com.yellobook.core.enums;

public enum AppMemberRole {
    ROLE_USER("사용자"),
    ROLE_ADMIN("관리자");

    private final String roleName;

    AppMemberRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
