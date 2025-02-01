package com.yellobook.storage.db.core.team;

public enum TeamMemberRole {
    ADMIN("관리자"),
    ORDERER("주문자"),
    VIEWER("뷰어");

    private final String description;

    TeamMemberRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

