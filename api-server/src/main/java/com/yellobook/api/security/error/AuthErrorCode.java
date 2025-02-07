package com.yellobook.api.security.error;

public enum AuthErrorCode {
    AUTH01("AUTH-01"),
    AUTH02("AUTH-02"),
    AUTH03("AUTH-03"),
    AUTH04("AUTH-04"),
    AUTH05("AUTH-05"),
    AUTH06("AUTH-06"),
    AUTH07("AUTH-07"),
    AUTH08("AUTH-08"),
    AUTH09("AUTH-09"),
    AUTH10("AUTH-10"),
    AUTH11("AUTH-11"),
    AUTH12("AUTH-12"),
    AUTH13("AUTH-13");
    private final String errorCode;

    AuthErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
