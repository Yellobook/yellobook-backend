package com.yellobook.api.controller.auth.enums;

public enum MemberRole {
    ROLE_USER("서비스 이용자"),
    ROLE_ADMIN("관리자");

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }
}
