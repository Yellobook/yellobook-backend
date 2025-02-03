package com.yellobook.admin.support.error;

public record AdminErrorMessage(
        String code,
        String message,
        Object data
) {
    public AdminErrorMessage(AdminErrorType errorType) {
        this(errorType.getCode(), errorType.getMessage(), null);
    }

    public AdminErrorMessage(AdminErrorType errorType, Object data) {
        this(errorType.getCode(), errorType.getMessage(), data);
    }
}
