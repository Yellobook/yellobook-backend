package com.yellobook.core.domain.team;

public enum ChangeRoleStatus {
    PENDING("대기 중"),
    ACCEPTED("승인 완료"),
    REJECTED("거절"),
    ;

    private final String description;

    ChangeRoleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
