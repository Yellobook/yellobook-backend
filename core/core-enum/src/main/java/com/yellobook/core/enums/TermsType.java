package com.yellobook.core.enums;

public enum TermsType {
    REQUIRED("필수"),
    OPTIONAL("선택");

    private final String displayName;

    TermsType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
