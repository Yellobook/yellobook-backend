package com.yellobook.core.enums;

public enum TeamMemberRole {
    SELLER("판매자"),
    ORDERER("주문자"),
    VIEWER("뷰어");

    private final String displayName;

    TeamMemberRole(String description) {
        this.displayName = description;
    }

    public String getDisplayName() {
        return displayName;
    }
}