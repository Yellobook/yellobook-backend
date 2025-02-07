package com.yellobook.admin.security;

public enum AdminMemberRole {
    ROLE_ADMIN("일반 관리자"),
    ROLE_SUPER_ADMIN("슈퍼 관리자");
    private final String label;

    AdminMemberRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

