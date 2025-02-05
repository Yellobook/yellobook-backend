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
    FILE008("FILE-008");

    private final String errorCode;

    ApiErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode;
    }
}
