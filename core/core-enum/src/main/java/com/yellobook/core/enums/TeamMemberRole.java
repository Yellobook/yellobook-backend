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

    public static TeamMemberRole fromDisplayName(String description) {
        for (TeamMemberRole role : values()) {
            if (role.displayName.equals(description)) {
                return role;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 Role의 description 입니다.");
    }
}