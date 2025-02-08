package com.yellobook.admin.support.error;

public enum AdminErrorCode {
    ADMIN01("ADMIN-01"),
    ADMIN02("ADMIN-02"),
    ADMIN03("ADMIN-03"),
    ADMIN04("ADMIN-04"),
    ADMIN05("ADMIN-05"),
    ADMIN06("ADMIN-06"),
    ADMIN07("ADMIN-07"),
    ADMIN08("ADMIN-08");

    private final String errorCode;

    AdminErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode;
    }
}
