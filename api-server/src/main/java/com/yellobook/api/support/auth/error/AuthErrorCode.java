package com.yellobook.api.support.auth.error;

public enum AuthErrorCode {
    AUTH001("AUTH-001"),
    AUTH002("AUTH-002"),
    AUTH003("AUTH-003"),
    AUTH004("AUTH-004"),
    AUTH005("AUTH-005"),
    AUTH006("AUTH-006"),
    AUTH007("AUTH-007"),
    AUTH008("AUTH-008"),
    AUTH009("AUTH-009"),
    AUTH010("AUTH-010"),
    AUTH011("AUTH-011"),
    AUTH012("AUTH-012"),
    AUTH013("AUTH-013"),
    AUTH014("AUTH-014"),
    AUTH015("AUTH015"),
    AUTH016("AUTH016");

    private final String errorCode;

    AuthErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
