package com.yellobook.api.support.error;

import com.yellobook.core.error.CoreErrorType;

public record ApiErrorMessage(
        String code,
        String message,
        Object data
) {
    public ApiErrorMessage(ApiErrorType errorType) {
        this(errorType.getCode(), errorType.getMessage(), null);
    }

    public ApiErrorMessage(ApiErrorType errorType, Object data) {
        this(errorType.getCode(), errorType.getMessage(), data);
    }

    public ApiErrorMessage(CoreErrorType errorType) {
        this(errorType.getCode(), errorType.getMessage(), null);
    }

    public ApiErrorMessage(CoreErrorType errorType, Object data) {
        this(errorType.getCode(), errorType.getMessage(), data);
    }
}
