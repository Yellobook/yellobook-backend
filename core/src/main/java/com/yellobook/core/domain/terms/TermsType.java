package com.yellobook.core.domain.terms;

import java.util.Arrays;

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

    public static TermsType fromName(String name) {
        return Arrays.stream(TermsType.values())
                .filter(type -> type.getName()
                        .equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 TermsType 이름: " + name));
    }
}
