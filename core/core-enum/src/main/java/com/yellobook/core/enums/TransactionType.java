package com.yellobook.core.enums;

public enum TransactionType {
    DELIVERY("택배거래"),
    DIRECT("직거래");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
