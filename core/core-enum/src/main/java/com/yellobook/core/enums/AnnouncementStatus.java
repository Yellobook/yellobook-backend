package com.yellobook.core.enums;

public enum AnnouncementStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String status;

    AnnouncementStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
