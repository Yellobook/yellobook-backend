package com.yellobook.api.support.error;

public enum ApiErrorCode {
    SYS001("SYS-001"),
    SYS002("SYS-002"),
    SYS003("SYS-003"),
    VAL001("VAL-001"),
    VAL002("VAL-002"),
    VAL003("VAL-003"),
    RES001("RES-001"),
    FILE001("FILE-001"),
    FILE002("FILE-002"),
    FILE003("FILE-003"),
    FILE004("FILE-004"),
    FILE005("FILE-005"),
    FILE006("FILE-006"),
    FILE007("FILE-007"),
    FILE008("FILE-008"),
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

    ApiErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode;
    }
}
