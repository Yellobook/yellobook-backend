package com.yellobook.core.enums;

public enum OrderStatus {
    PENDING("승인 대기중"),
    APPROVED("주문 진행중"),
    COMPLETED("거래 완료"),
    REJECTED("주문 반려"),
    CANCELED("주문 취소");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

