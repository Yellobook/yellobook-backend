package com.yellobook.api.controller.auth.enums;

public enum TokenType {
    ACCESS("엑세스 토큰"),
    REFRESH("리프레시 토큰"),
    TERMS("약관 동의 토큰");

    private final String description;

    TokenType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
