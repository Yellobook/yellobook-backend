package com.yellobook.api.support.error;

public enum ApiErrorCode {
    SYS01("SYS-01"),
    SYS02("SYS-02"),
    SYS03("SYS-03"),
    VAL01("VAL-01"),
    VAL02("VAL-02"),
    VAL03("VAL-03"),
    RES01("RES-01"),
    FILE01("FILE-01"),
    FILE02("FILE-02"),
    FILE03("FILE-03"),
    FILE04("FILE-04"),
    FILE05("FILE-05"),
    FILE06("FILE-06"),
    FILE07("FILE-07"),
    FILE08("FILE-08");

    private final String errorCode;

    ApiErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode;
    }
}
