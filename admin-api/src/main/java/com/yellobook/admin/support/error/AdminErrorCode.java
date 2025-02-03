package com.yellobook.admin.support.error;

public enum AdminErrorCode {
    SYS01("SYS-01"),
    SYS02("SYS-02"),
    SYS03("SYS-03"),
    VAL01("VAL-01"),
    VAL02("VAL-02"),
    VAL03("VAL-03"),
    RES01("RES-01"),
    TERMS01("TERMS-01"),
    TERMS02("TERMS-02"),
    TERMS03("TERMS-03"),
    TERMS04("TERMS-04");

    private final String errorCode;

    AdminErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode;
    }
}
