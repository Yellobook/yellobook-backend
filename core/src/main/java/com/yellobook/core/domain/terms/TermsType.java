package com.yellobook.core.domain.terms;

public enum TermsType {
    REQUIRED("필수"),
    OPTIONAL("선택");

    private final String name;

    TermsType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
