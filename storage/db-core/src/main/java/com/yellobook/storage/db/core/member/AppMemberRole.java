package com.yellobook.storage.db.core.member;

public enum AppMemberRole {
    ROLE_USER("서비스 이용자"),
    ROLE_ADMIN("관리자");

    private final String description;

    AppMemberRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
